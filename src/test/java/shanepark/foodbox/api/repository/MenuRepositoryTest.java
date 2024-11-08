package shanepark.foodbox.api.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shanepark.foodbox.api.domain.MenuResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MenuRepositoryTest {

    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        menuRepository = new MenuRepository();
    }

    @Test
    void save_shouldStoreMenuResponse() {
        MenuResponse menuResponse = new MenuResponse(LocalDate.now(), List.of("Breakfast", "Lunch", "Dinner"));

        menuRepository.save(menuResponse);
        Optional<MenuResponse> retrieved = menuRepository.findByDate(menuResponse.date());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get()).isEqualTo(menuResponse);
        assertThat(retrieved.get().menus()).containsExactly("Breakfast", "Lunch", "Dinner");
    }

    @Test
    void findByDate_shouldReturnEmptyOptionalIfDateNotFound() {
        Optional<MenuResponse> retrieved = menuRepository.findByDate(LocalDate.now());

        assertThat(retrieved).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllSavedMenus() {
        MenuResponse menu1 = new MenuResponse(LocalDate.now(), List.of("Breakfast", "Lunch"));
        MenuResponse menu2 = new MenuResponse(LocalDate.now().plusDays(1), List.of("Brunch", "Supper"));
        menuRepository.save(menu1);
        menuRepository.save(menu2);

        List<MenuResponse> allMenus = menuRepository.findAll();

        assertThat(allMenus).containsExactlyInAnyOrder(menu1, menu2);
    }

    @Test
    void update_shouldReplaceExistingMenuResponse() {
        LocalDate date = LocalDate.now();
        MenuResponse original = new MenuResponse(date, List.of("Original Breakfast", "Original Lunch"));
        MenuResponse updated = new MenuResponse(date, List.of("Updated Breakfast", "Updated Dinner"));

        menuRepository.save(original);
        menuRepository.update(updated);

        Optional<MenuResponse> retrieved = menuRepository.findByDate(date);

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get()).isEqualTo(updated);
        assertThat(retrieved.get().menus()).containsExactly("Updated Breakfast", "Updated Dinner");
    }

    @Test
    void update_shouldNotCreateNewEntryIfDateDoesNotExist() {
        LocalDate date = LocalDate.now();
        MenuResponse updated = new MenuResponse(date, List.of("Updated Breakfast", "Updated Dinner"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> menuRepository.update(updated));
    }

    @Test
    void delete_shouldRemoveMenuResponseForGivenDate() {
        LocalDate date = LocalDate.now();
        MenuResponse menuResponse = new MenuResponse(date, List.of("Breakfast", "Lunch"));

        menuRepository.save(menuResponse);
        menuRepository.delete(date);

        Optional<MenuResponse> retrieved = menuRepository.findByDate(date);

        assertThat(retrieved).isEmpty();
    }

    @Test
    void delete_shouldNotThrowExceptionIfDateDoesNotExist() {
        LocalDate date = LocalDate.now();
        menuRepository.delete(date);
        assertThat(menuRepository.findAll()).isEmpty();
    }

    @Test
    void findAll_shouldReturnEmptyListIfNoMenusSaved() {
        List<MenuResponse> allMenus = menuRepository.findAll();

        assertThat(allMenus).isEmpty();
    }
}

