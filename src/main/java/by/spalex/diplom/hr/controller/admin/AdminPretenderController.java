package by.spalex.diplom.hr.controller.admin;

import by.spalex.diplom.hr.model.Pretender;
import by.spalex.diplom.hr.model.Skill;
import by.spalex.diplom.hr.service.PretenderService;
import by.spalex.diplom.hr.service.SkillService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminPretenderController {

    private final PretenderService pretenderService;

    private final SkillService skillService;

    private Skill skill;
    private String name;

    @Autowired
    public AdminPretenderController(PretenderService pretenderRepository, SkillService skillRepository) {
        this.pretenderService = pretenderRepository;
        this.skillService = skillRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new DoublePropertyEditor());
    }

    @RequestMapping(value = "/admin/pretenders", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("edited_item", new Pretender());
        modelAndView.addObject("pretenderCount", pretenderService.count());
        return modelAndView;
    }

    private ModelAndView getModelAndView() {
        ModelAndView modelAndView = new ModelAndView("admin/main");
        modelAndView.addObject("page", "admin/pretender");
        List<Skill> skills = new ArrayList<>();
        skills.add(null);
        skills.addAll(skillService.findAll());
        modelAndView.addObject("skills", skills);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/pretender/edit", method = RequestMethod.POST)
    @Transactional
    public String pretenderEdit(@RequestParam(value = "image_file", required = false) MultipartFile file,
                                ModelMap map,
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
            if (pretenderService.save(pretender) != null) {
                map.addAttribute("message", "Анкета сохранена");
            }
        }
        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name));
        return "admin/pretender_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/pretenders", method = RequestMethod.POST)
    public String getPretenders(ModelMap map,
                                @RequestParam(value = "filter_name", required = false) String name,
                                @RequestParam(value = "filter_skill", required = false) Skill skill) {
        this.name = name;
        this.skill = skill;
        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name));
        return "admin/pretender_frags :: table_fragment";
    }


    @RequestMapping(value = "/admin/pretender/delete", method = RequestMethod.POST)
    @Transactional
    public String deletePretender(ModelMap map,
                                  @RequestParam(value = "id") Long id) {

        if (pretenderService.delete(id)) {
            map.addAttribute("message", "Анкета удалена");
        }
        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name));
        return "admin/pretender_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/pretender", params = "id", method = RequestMethod.GET)
    public String getPretender(ModelMap map,
                               @RequestParam(name = "id") Long id) {
        Pretender pretender = pretenderService.getPretender(id);
        map.addAttribute("edited_item", pretender);
        return "admin/pretender_frags :: edit_fragment";
    }

    @RequestMapping(value = "/admin/pretender/view", method = RequestMethod.GET)
    public String getView(ModelMap map,
                          @RequestParam(name = "id") Long id) {

        Pretender pretender = pretenderService.findOne(id);
        if (pretender != null) {
            map.addAttribute("view_item", pretender);
        } else {
            map.addAttribute("view_item", "");
        }

        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name));
        return "admin/pretender_frags :: view_fragment";
    }

}
