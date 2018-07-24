package by.spalex.diplom.hr.controller.admin;

import by.spalex.diplom.hr.model.Skill;
import by.spalex.diplom.hr.service.SkillService;
import by.spalex.diplom.hr.tools.DoublePropertyEditor;
import by.spalex.diplom.hr.tools.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Controller
public class AdminSkillController {

    private final SkillService skillService;

    @Autowired
    public AdminSkillController(SkillService skillRepository) {
        this.skillService = skillRepository;
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new DoublePropertyEditor());
    }

    @RequestMapping(value = "/admin/skill", method = RequestMethod.GET)
    public ModelAndView skill() {
        ModelAndView modelAndView = new ModelAndView("admin/main");
        modelAndView.addObject("page", "admin/skill");
        modelAndView.addObject("max_priority", (int) Util.round(getMaxPriority() * 100, 0));
        modelAndView.addObject("edited_item", new Skill());
        return modelAndView;
    }

    @RequestMapping(value = "/admin/skill/edit", method = RequestMethod.POST)
    public String skillEdit(ModelMap map, @ModelAttribute(value = "edited_item") Skill skill,
                            @ModelAttribute(value = "percent_priority") int priority,
                            @ModelAttribute(value = "raw_values") String rawValues
    ) {
        if (Util.isEmpty(skill.getName())) {
            map.addAttribute("message", "Не заполнено имя");
        } else if (priority < 1) {
            map.addAttribute("message", "Не заполнен приоритет");
        } else if (Util.isEmpty(rawValues)) {
            map.addAttribute("message", "Не заданы значения");
        } else {
            try {
                if (skillService.save(skill, priority, rawValues) != null) {
                    map.addAttribute("message", "Критерий сохранен");
                }
            } catch (ParseException e) {
                map.addAttribute("message", e.toString());
            }
        }
        map.addAttribute("max_priority", (int) Util.round(getMaxPriority() * 100, 0));
        map.addAttribute("skills", skillService.findAll());
        return "admin/skill_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/skills", method = RequestMethod.GET)
    public String getSkills(ModelMap map) {
        map.addAttribute("skills", skillService.findAll());
        map.addAttribute("max_priority", (int) Util.round(getMaxPriority() * 100, 0));
        return "admin/skill_frags :: table_fragment";
    }

    @RequestMapping(value = "/admin/skill/delete", method = RequestMethod.GET)
    @Transactional
    public String deleteSkill(ModelMap map,
                              @RequestParam(value = "id") Long id) {
        if (skillService.delete(id)) {
            map.addAttribute("message", "Критерий удален");
        }
        map.addAttribute("skills", skillService.findAll());
        return "admin/skill_frags :: table_fragment";
    }


    @RequestMapping(value = "/admin/skill", params = {"id"}, method = RequestMethod.GET)
    public String getSkill(ModelMap map, @RequestParam(name = "id") Long id) {

        Skill skill = skillService.getOrCreate(id);
        int priority = (int) Util.round(skill.getPriority() * 100, 0);
        int max = (int) Util.round(getMaxPriority() * 100, 0);
        max = Math.max(max, priority);
        map.addAttribute("percent_priority", priority);
        map.addAttribute("max_priority", max);
        map.addAttribute("raw_values", skill.getRawValues());
        map.addAttribute("edited_item", skill);
        return "admin/skill_frags :: edit_fragment";
    }

    private double getMaxPriority() {
        List<Skill> skills = skillService.findAll();
        double overallPriority = 0.0;
        for (Skill item : skills) {
            overallPriority += item.getPriority();
        }
        double maxPriority = 1.0 - overallPriority;
        if (maxPriority < 0) {
            maxPriority = 0.0;
        }
        return maxPriority;
    }

}
