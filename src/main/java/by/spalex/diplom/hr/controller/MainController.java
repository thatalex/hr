package by.spalex.diplom.hr.controller;

import by.spalex.diplom.hr.model.User;
import by.spalex.diplom.hr.service.UserService;
import by.spalex.diplom.hr.tools.Logger;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main site's controller
 */
@Controller
@ControllerAdvice
public class MainController {

    private final UserService userService;


    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handle main page of site
     *
     * @return filled with User entity ModelAndView
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(ModelMap map) {
        map.addAttribute("user", new User());
        return "home";
    }

    /**
     * Handler authorization page request
     *
     * @param error if true add to page erros message
     * @return filled ModelAndView object
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(name = "error", required = false) Boolean error) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        if (error != null && error) {
            modelAndView.addObject("message", "Неверный логин или пароль");
        }

        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registrationPost(@ModelAttribute("passRepeat") String passRepeat,
                                         @ModelAttribute("user") User user,
                                         HttpServletRequest httpRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        User userExists = userService.findOneByEmail(user.getEmail());
        if (!user.nameIsValid()){
            modelAndView.addObject("message", "Имя содержит недопустимые символы");
        } else if (!user.getPassword().equals(passRepeat)) {
            modelAndView.addObject("message", "Пароли не совпадают");
        } else if (userExists != null) {
            modelAndView.addObject("message", "Эл. почта уже зарегистрирована");
        } else {
            userService.savePretender(user);
            modelAndView.addObject("email", user.getEmail());
            modelAndView.addObject("password", passRepeat);
            modelAndView.setViewName("home");
            Logger.INSTANCE.info("User registration: " + user.getEmail());
            try {
                httpRequest.login(user.getEmail(), passRepeat);
                modelAndView.setViewName("redirect:/pretender/home");
            } catch (ServletException e) {
                modelAndView.addObject("message", e.toString());
            }
        }
        return modelAndView;
    }

    /**
     * Handle forbidden error
     *
     * @return filled ModelAndView object
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView error403() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/403");
        return modelAndView;
    }


    /**
     * Handle not found error
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView notFound() {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error_message", "Страница не найдена");
        return modelAndView;
    }

    /**
     * Handle bad request error
     */
    @ExceptionHandler(BadHttpRequest.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequest() {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error_message", "Неверный запрос");
        return modelAndView;
    }

    /**
     * Handle exceptions
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        ModelAndView modelAndView = new ModelAndView("error");
        String msg = "Возникла внутренняя ошибка при запросе " + request.getRequestURL() + ":\r\n" + exception.toString();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
        modelAndView.addObject("frag_message", msg);
        Logger.INSTANCE.error(msg);
        return modelAndView;
    }
}
