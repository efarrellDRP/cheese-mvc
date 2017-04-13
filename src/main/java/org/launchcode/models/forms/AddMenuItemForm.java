package org.launchcode.models.forms;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;

import javax.validation.constraints.NotNull;

/**
 * Created by Ezekiel on 4/10/17.
 */

public class AddMenuItemForm {
    private Menu menu;
    private Iterable<Cheese> cheeses;

    public int getMenuId() {
        return menuId;
    }

    @NotNull

    private int menuId;

    public int getCheeseId() {
        return cheeseId;
    }

    @NotNull
    private int cheeseId;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Iterable<Cheese> getCheeses() {
        return cheeses;
    }

    public void setCheeses(Iterable<Cheese> cheeses) {
        this.cheeses = cheeses;
    }

    public AddMenuItemForm() {
    }


    public AddMenuItemForm(Menu menu,Iterable<Cheese> cheeses) {
        this.menu = menu;
        this.cheeses = cheeses;
    }
}
