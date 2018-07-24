package by.spalex.diplom.hr.controller.admin;

import by.spalex.diplom.hr.model.LogItem;
import by.spalex.diplom.hr.model.Role;
import by.spalex.diplom.hr.model.User;
import by.spalex.diplom.hr.service.LogService;
import by.spalex.diplom.hr.service.UserService;
import by.spalex.diplom.hr.tools.Logger;
import by.spalex.diplom.hr.tools.Util;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Admin's pages controller
 */
@Controller
public class AdminController {

    private static final String BACKUP_FILENAME = "backup.sql";

    private final UserService userService;

    private final LogService logService;

    @Autowired
    public AdminController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    /**
     * main admin's page handler
     *
     * @return ModelAndView object
     */
    @RequestMapping(value = {"/admin", "/admin/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        return getHomeView("", "", null);
    }

    private ModelAndView getHomeView(String userName, String userEmail, Role userRole) {
        ModelAndView modelAndView = new ModelAndView("admin/main");
        modelAndView.addObject("page", "admin/home");
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("userEmail", userEmail);
        List<Role> roles = new ArrayList<>();
        roles.add(null);
        roles.addAll(Arrays.asList(Role.values()));
        modelAndView.addObject("roles", roles);
        List<User> users = userService.getUsers(userName, userEmail, userRole);
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.POST)
    public String getUsers(ModelMap map,
                           @RequestParam(value = "user_name", required = false) String userName,
                           @RequestParam(value = "user_email", required = false) String userEmail,
                           @RequestParam(value = "user_role", required = false) Role userRole) {
        ModelAndView modelAndView = getHomeView(userName, userEmail, userRole);
        map.addAllAttributes(modelAndView.getModel());
        return "admin/home_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/user/edit", method = {RequestMethod.POST})
    public String userEdit(ModelMap map,
                           Principal principal,
                           @RequestParam(value = "user_name", required = false) String userName,
                           @RequestParam(value = "user_email", required = false) String userEmail,
                           @RequestParam(value = "user_role", required = false) Role userRole,
                           @RequestParam(value = "passRepeat", required = false) String passRepeat,
                           @ModelAttribute(value = "edited_item") User user) {

        User currentUser = userService.findOneByEmail(principal.getName());
        if (currentUser.getEmail().equals(user.getEmail()) && currentUser.getRole().equals(Role.ADMIN) && !user.getRole().equals(Role.ADMIN)) {
            map.addAttribute("message", "Снятие самому себе роли администратора запрещено");
        } else if (Util.isEmpty(user.getEmail())) {
            map.addAttribute("message", "Эл. почта не заполнена");
        } else if (Util.isEmpty(user.getName())) {
            map.addAttribute("message", "Имя не заполнено");
        } else if (Util.isEmpty(user.getPassword()) && (user.getId() == null || user.getId().equals(0L))) {
            map.addAttribute("message", "Пароль не заполнен");
        } else if (!Util.isEmpty(user.getPassword()) && (!user.getPassword().equals(passRepeat))) {
            map.addAttribute("message", "Пароли не совпадают");
        } else if ((user.getId() == null || user.getId() < 1) && userService.findOneByEmail(user.getEmail()) != null) {
            map.addAttribute("message", "Эл. почта уже зарегистрирована");
        } else {
            if (userService.save(user) != null) {
                map.addAttribute("message", "Пользователь сохранен");
            }
        }
        ModelAndView modelAndView = getHomeView(userName, userEmail, userRole);
        map.addAllAttributes(modelAndView.getModel());
        return "admin/home_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/user/delete", method = RequestMethod.POST)
    @Transactional
    public String deleteUser(ModelMap map,
                             Principal principal,
                             @RequestParam(value = "id") Long id,

                             @RequestParam(value = "user_name", required = false) String userName,
                             @RequestParam(value = "user_email", required = false) String userEmail,
                             @RequestParam(value = "user_role", required = false) Role userRole) {
        User currentUser = userService.findOneByEmail(principal.getName());
        if (currentUser.getId().equals(id)) {
            map.addAttribute("message", "Удаление самого себя запрещено");
        } else {
            userService.delete(id);
        }
        ModelAndView modelAndView = getHomeView(userName, userEmail, userRole);
        map.addAllAttributes(modelAndView.getModel());
        return "admin/home_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.GET)
    public String getUser(ModelMap map,
                          @RequestParam(name = "id") Long id) {

        User user;
        if (id != null && id > 0) {
            user = userService.findOne(id);
        } else {
            user = new User();
            user.setRole(Role.PRETENDER);
        }
        map.addAttribute("edited_item", user);
        return "admin/home_frags :: edit_fragment";
    }

    /**
     * Handle admin's maintance page
     *
     * @return filled ModelAndView object
     */
    @RequestMapping(value = "/admin/maintance", method = RequestMethod.GET)
    public ModelAndView maintance() {
        ModelAndView modelAndView = new ModelAndView("admin/main");
        modelAndView.addObject("page", "admin/maintance");
        List<LogItem> logItems = logService.findAll();
        Logger.sort(logItems);
        modelAndView.addObject("logitems", logItems);
        return modelAndView;
    }

    @Autowired
    private EntityManager entityManager;

    /**
     * Create and return to response backup's script of database
     *
     * @param response servlet response
     */
    @RequestMapping(value = "/admin/backup", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void getFile(HttpServletResponse response) {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(String.format("SCRIPT TO '%s'", BACKUP_FILENAME));
                File file = new File(BACKUP_FILENAME);
                if (file.exists()) {
                    response.setContentType("application/x-msdownload");
                    response.setHeader("Content-disposition", "attachment; filename=" + BACKUP_FILENAME);
                    try (InputStream is = new FileInputStream(file)) {
                        IOUtils.copy(is, response.getOutputStream());
                        response.flushBuffer();
                        Logger.INSTANCE.info("Database was backuped");
                    } catch (IOException ex) {
                        Logger.INSTANCE.error(ex.getMessage());
                    }
                }
            } catch (SQLException e) {
                Logger.INSTANCE.error(e.getMessage());
            }
        });
    }
}
