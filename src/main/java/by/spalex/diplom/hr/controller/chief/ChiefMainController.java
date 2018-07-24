package by.spalex.diplom.hr.controller.chief;

import by.spalex.diplom.hr.model.Pretender;
import by.spalex.diplom.hr.model.Skill;
import by.spalex.diplom.hr.service.PretenderService;
import by.spalex.diplom.hr.service.SkillService;
import by.spalex.diplom.hr.service.SkillValueService;
import by.spalex.diplom.hr.tools.DoublePropertyEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChiefMainController {

    private final PretenderService pretenderService;

    private final SkillService skillService;

    private final SkillValueService skillValueService;

    private Skill skill;
    private String name;
    private Integer age_from;
    private Integer age_till;

    @Autowired
    public ChiefMainController(PretenderService pretenderService, SkillService skillService,
                               SkillValueService skillValueService) {
        this.pretenderService = pretenderService;
        this.skillService = skillService;
        this.skillValueService = skillValueService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new DoublePropertyEditor());
    }

    @RequestMapping(value = {"/chief", "/chief/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("edited_item", new Pretender());
        modelAndView.addObject("skillValues", skillValueService.findAll());
        modelAndView.addObject("pretenderCount", pretenderService.count());
        return modelAndView;
    }

    private ModelAndView getModelAndView() {
        ModelAndView modelAndView = new ModelAndView("chief/main");
        modelAndView.addObject("page", "chief/home");
        List<Skill> skills = new ArrayList<>();
        skills.add(null);
        skills.addAll(skillService.findAll());
        modelAndView.addObject("skills", skills);

        return modelAndView;
    }

    @RequestMapping(value = "/chief/pretenders", method = RequestMethod.POST)
    public String getPretenders(ModelMap map,
                                @RequestParam(value = "filter_name", required = false) String name,
                                @RequestParam(value = "filter_skill", required = false) Skill skill,
                                @RequestParam(value = "age_from", required = false) Integer age_from,
                                @RequestParam(value = "age_till", required = false) Integer age_till) {
        this.name = name;
        this.skill = skill;
        this.age_from = age_from;
        this.age_till = age_till;
        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name, age_from, age_till));
        return "chief/home_frags :: table_fragment";
    }


    @RequestMapping(value = "/chief/pretender/delete", method = RequestMethod.GET)
    @Transactional
    public String deletePretender(ModelMap map,
                                  @RequestParam(value = "id") Long id) {
        if (pretenderService.delete(id)) {
            map.addAttribute("message", "Анкета удалена");
        }
        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name, age_from, age_till));
        return "chief/home_frags :: table_fragment";
    }

    @RequestMapping(value = "/chief/pretender/invite", method = RequestMethod.GET)
    @Transactional
    public String invitePretender(ModelMap map,
                                  @RequestParam(value = "id") Long id) {
        if (pretenderService.invite(id)){
            map.addAttribute("message", "Приглашение отправлено");
        }
        map.addAttribute("pretenders", pretenderService.getSortedPretenders(skill, name, age_from, age_till));
        return "chief/home_frags :: table_fragment";
    }

    @RequestMapping(value = "/chief/pretender", params = {"id"}, method = RequestMethod.GET)
    public String getPretender(ModelMap map,
                               @RequestParam(name = "id") Long id) {
        Pretender pretender = pretenderService.findOne(id);
        if (pretender == null) {
            pretender = new Pretender();
        }
        map.addAttribute("pretender", pretender);
        return "chief/home_frags :: view_fragment";
    }

}