package manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    @DisplayName("Тест инициализации менеджеров по умолчанию")
    void testGetDefaultManagers() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager, "Менеджер задач должен быть инициализирован.");
        assertNotNull(historyManager, "Менеджер истории должен быть инициализирован.");
    }
}
