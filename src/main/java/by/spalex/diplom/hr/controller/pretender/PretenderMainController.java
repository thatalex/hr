package by.spalex.diplom.hr.controller.pretender;

import by.spalex.diplom.hr.model.Pretender;
import by.spalex.diplom.hr.model.User;
import by.spalex.diplom.hr.repository.UserRepository;
import by.spalex.diplom.hr.service.PretenderService;
import by.spalex.diplom.hr.tools.DoublePropertyEditor;
import by.spalex.diplom.hr.tools.Logger;
import by.spalex.diplom.hr.tools.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
public class PretenderMainController {

    private final PretenderService pretenderService;

    private final UserRepository userRepository;

    @Autowired
    public PretenderMainController(PretenderService pretenderService, UserRepository userRepository) {
        this.pretenderService = pretenderService;
        this.userRepository = userRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new DoublePropertyEditor());
    }

    @RequestMapping(value = {"/pretender", "/pretender/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("pretender/main");
        modelAndView.addObject("page", "pretender/home");
        return modelAndView;
    }

    @RequestMapping(value = "/pretender/pretender", method = RequestMethod.GET)
    public String getPretender(ModelMap map, Principal principal) {
        User user = userRepository.findOneByEmail(principal.getName());
        Pretender pretender = pretenderService.getPretender(user);
        map.addAttribute("edited_item", pretender);
        return "pretender/home_frags :: edit_fragment";
    }

    @RequestMapping(value = "/pretender/pretender/edit", method = RequestMethod.POST)
    @Transactional
    public String pretenderEdit(@RequestParam(value = "image_file", required = false) MultipartFile file,
                                ModelMap map, Principal principal,
                                @Valid @ModelAttribute(value = "edited_item") Pretender pretender) {
        if (Util.isEmpty(pretender.getUser().getName())) {
            map.addAttribute("message", "Не заполнено имя");
        } else if (!pretender.getUser().nameIsValid()) {
            map.addAttribute("message", "Имя содержит недопустимые символы");
        } else if (Util.isEmpty(pretender.getAddress())) {
            map.addAttribute("message", "Не заполнена адрес");
        } else if (Util.isEmpty(pretender.getPhone())) {
            map.addAttribute("message", "Не заполнен телефон");
        } else if (!pretender.phoneIsValid()) {
            map.addAttribute("message", "Номер содержит недопустимые символы");
        } else if (file == null && pretender.getId() < 1) {
            map.addAttribute("message", "Не выбран файл");
        } else {
            try {
                Util.setImage(file, pretender);
            } catch (IOException e) {
                Logger.INSTANCE.error(e.getMessage());
                map.addAttribute("message", "Ошибка чтения файла: " + e.toString());
            }

            if (pretender.getUser() == null) {
                User user = userRepository.findOneByEmail(principal.getName());
                pretender.setUser(user);
            }
            if (pretenderService.save(pretender) != null) {
                map.addAttribute("message", "Анкета сохранена");
            }
        }
        map.addAttribute("edited_item", pretender);
        return "pretender/home_frags :: edit_fragment";
    }
}
