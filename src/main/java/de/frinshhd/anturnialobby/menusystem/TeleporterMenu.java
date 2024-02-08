package de.frinshhd.anturnialobby.menusystem;

import de.frinshhd.anturnialobby.Main;
import de.frinshhd.anturnialobby.Manager;
import de.frinshhd.anturnialobby.menusystem.library.Menu;
import de.frinshhd.anturnialobby.model.Config;
import de.frinshhd.anturnialobby.model.Server;
import de.frinshhd.anturnialobby.utils.ItemTags;
import de.frinshhd.anturnialobby.utils.MessageFormat;
import de.frinshhd.anturnialobby.utils.SpigotTranslator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

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
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR) ){
            return;
        }

        String id = ItemTags.extractItemId(item.getItemMeta());

        if (id == null) {
            return;
        }

        Server server = null;

        for (Server servers : config.getTeleporter().getServers()) {
            if (servers.getId().equals(id)) {
                server = servers;
                break;
            }
        }

        if (server == null) {
            return;
        }

        player.sendMessage(server.getName());

        server.execute(player);
        if (server.getMessage() != null) {
            player.sendMessage(MessageFormat.build(server.getMessage()));
        }
    }

    @Override
    public ItemStack getFillerItem() {
        return config.getTeleporter().getFillerItem().getItem();
    }

    @Override
    public void open() {
        open(config.getTeleporter().getFillerItem().getFillerType());
    }
}
