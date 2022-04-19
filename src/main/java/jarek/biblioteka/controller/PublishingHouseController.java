package jarek.biblioteka.controller;

import jarek.biblioteka.model.PublishingHouse;
import jarek.biblioteka.service.PublishingHouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(path = "/edit/{id}")
    public String edit(Model model,
                       @PathVariable(name = "id") Long editedId) {
        Optional<PublishingHouse> optionalPublishingHouse = publishingHouseService.getById(editedId);
        if (optionalPublishingHouse.isPresent()) {
            model.addAttribute("at_publishingHouse", optionalPublishingHouse.get());

            return "ph-add";
        }

        return "redirect:/ph/list";
    }

    @GetMapping(path = "/delete/{id}")
    public String remove(@PathVariable(name = "id") Long deletedId) {
        publishingHouseService.removeById(deletedId);

        return "redirect:/ph/list";
    }

    @GetMapping(path = "/books/{id}")
    public String getBooks(Model model,
                           @PathVariable(name = "id") Long PH_Id) {
        Optional<PublishingHouse> optionalPublishingHouse = publishingHouseService.getById(PH_Id);
        if (optionalPublishingHouse.isPresent()) {
            model.addAttribute("at_books", optionalPublishingHouse.get().getBooks());

            return "book-list";
        }
        return "redirect:/ph/list";
    }
}
