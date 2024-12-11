package shanepark.foodbox.api.repository;

import org.springframework.stereotype.Repository;
import shanepark.foodbox.api.domain.MenuResponse;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MenuRepository {

    private final ConcurrentHashMap<LocalDate, MenuResponse> menuMap = new ConcurrentHashMap<>();

    public Optional<MenuResponse> findByDate(LocalDate date) {
        return Optional.ofNullable(menuMap.get(date));
    }

    public List<MenuResponse> findAll() {
        return menuMap.values().stream()
                .sorted(Comparator.comparingLong(m -> m.date().toEpochDay()))
                .toList().reversed();
    }

    public void save(MenuResponse menuResponse) {
        menuMap.put(menuResponse.date(), menuResponse);
    }

    public void update(MenuResponse menuResponse) {
        if (!menuMap.containsKey(menuResponse.date())) {
            throw new IllegalArgumentException("Menu not found");
        }
        menuMap.put(menuResponse.date(), menuResponse);
    }

    public void delete(LocalDate date) {
        menuMap.remove(date);
    }

}
