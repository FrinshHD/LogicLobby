import de.frinshhd.logiclobby.menusystem.TeleporterMenu;
import org.junit.Test;

import static org.junit.Assert.*;

public class TeleportMenuTest {

    @Test
    public void testIsService() {
        assertTrue(TeleporterMenu.isService("SkyBlock-1"));
    }

}
