package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.CheeseData;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Ezekiel on 4/10/17.
 */

@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(value="")
    public String index(Model model){
        model.addAttribute("menus",menuDao.findAll());
        model.addAttribute("title","Menus");
        return "menu/index";
    }

    @RequestMapping(value="add",method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("menu", new Menu());
        model.addAttribute("title","Add Menu");
        return "menu/add";
    }
    @RequestMapping(value="add",method = RequestMethod.POST)
    public String processAdd(Model model, @ModelAttribute @Valid Menu newMenu, Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/"+(newMenu.getId());
    }

    @RequestMapping(value="view/{id}", method=RequestMethod.GET)
    public String viewMenu(Model model,@PathVariable int id){
        model.addAttribute("menu",menuDao.findOne(id));
        model.addAttribute("title",menuDao.findOne(id).getName());
        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}", method=RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id){
        Menu menu=menuDao.findOne(id);
        model.addAttribute("menu",menu);
        AddMenuItemForm form= new AddMenuItemForm(menu, CheeseData.getAll());
        model.addAttribute("form",form);
        model.addAttribute("title","Add item to menu: " + menu.getName());
        return "menu/add-item";
    }

    @RequestMapping(value="add-item",method = RequestMethod.POST)
    public String addItem(Model model, @Valid AddMenuItemForm addMenuItemForm,  Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu");
            return "menu/add-item";
        }
        Menu menu=menuDao.findOne(addMenuItemForm.getMenuId());
        Cheese cheese=CheeseData.getById(addMenuItemForm.getCheeseId());
        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view/"+menu.getId();
    }

}
