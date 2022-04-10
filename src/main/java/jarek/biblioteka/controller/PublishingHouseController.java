package jarek.biblioteka.controller;

import jarek.biblioteka.model.PublishingHouse;
import jarek.biblioteka.service.PublishingHouseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/ph/")
public class PublishingHouseController {

    private final PublishingHouseService publishingHouseService;

    @GetMapping(path = "/list")
    public String getList(Model model) {
        List<PublishingHouse> publishingHouseList = publishingHouseService.getAll();
        model.addAttribute("at_PH_List", publishingHouseList);

        return "ph-list";
    }

    @GetMapping(path = "/add")
    public String addPublishingHouse(Model model, PublishingHouse publishingHouse) {
        model.addAttribute("at_publishingHouse", publishingHouse);

        return "ph-add";
    }

    @PostMapping(path = "/add")
    public String savePublishingHouse(PublishingHouse publishingHouse) {
        publishingHouseService.save(publishingHouse);

        return "redirect:/ph/list";
    }
}
