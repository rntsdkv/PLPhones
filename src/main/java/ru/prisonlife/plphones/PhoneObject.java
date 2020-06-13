package ru.prisonlife.plphones;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PhoneObject {

    private Player player;
    private Integer number;
    private Integer money;

    public PhoneObject(Player player, Integer number, Integer money) {
        this.player = player;
        this.number = number;
        this.money = money;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
