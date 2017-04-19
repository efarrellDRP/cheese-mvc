package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
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

    @Autowired
    private CheeseDao cheeseDao;
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

    @RequestMapping(value="view/{menuId}", method=RequestMethod.GET)
    public String viewMenu(Model model,@PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        model.addAttribute("cheeses",menu.getCheese());
        model.addAttribute("menuId",menu.getId());
        model.addAttribute("title",menuDao.findOne(menuId).getName());
        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}", method=RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id){
        Menu menu=menuDao.findOne(id);
        AddMenuItemForm form= new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form",form);
        model.addAttribute("title","Add item to menu: " + menu.getName());
        return "menu/add-item";
    }

    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String addItem(Model model,
                          @Valid AddMenuItemForm form,
                          Errors errors

                          ){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu");
            return "menu/add-item";
        }

        Menu menu=menuDao.findOne(form.getMenuId());

        Cheese cheese=cheeseDao.findOne(form.getCheeseId());
        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view/"+menu.getId();
    }

}
