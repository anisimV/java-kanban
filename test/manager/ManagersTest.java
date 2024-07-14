package manager;

import manager.Managers;
import manager.TaskManager;
import manager.HistoryManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void testGetDefaultManagers() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager, "Менеджер задач должен быть инициализирован.");
        assertNotNull(historyManager, "Менеджер истории должен быть инициализирован.");
    }
}
