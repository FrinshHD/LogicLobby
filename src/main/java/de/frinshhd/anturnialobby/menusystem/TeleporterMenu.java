package de.frinshhd.anturnialobby.menusystem;

import de.frinshhd.anturnialobby.Main;
import de.frinshhd.anturnialobby.Manager;
import de.frinshhd.anturnialobby.menusystem.library.Menu;
import de.frinshhd.anturnialobby.model.Config;
import de.frinshhd.anturnialobby.utils.SpigotTranslator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TeleporterMenu extends Menu {

    private final Config config = Main.getManager().getConfig();

    private Manager manager;

    public TeleporterMenu(Player player) {
        super(player);
    }

    @Override
    public String getTitle() {
        return SpigotTranslator.build("teleporter.title");
    }

    @Override
    public int getSlots() {
        return config.getTeleporter().getInventorySlots();
    }

    @Override
    public void setItems() {
        //check if CloudNet is used or not
        if (config.hasCloudNetSupportEnabled()) {
            //Todo: CloudNet integration

        } else {
            config.getTeleporter().getServers().forEach(server -> {
                if (server.getItemSlot() > -1) {
                    inventory.setItem(server.getItemSlot(), server.getItem());
                }
            });
        }

    }

    @Override
    public void handle(InventoryClickEvent event) {

    }

    @Override
    public void fillBorders(ItemStack item) {
        super.fillBorders(config.getTeleporter().getFillerItem().getItem());
    }
}
