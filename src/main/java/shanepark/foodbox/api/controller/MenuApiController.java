package shanepark.foodbox.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shanepark.foodbox.api.domain.ApiResponse;
import shanepark.foodbox.api.domain.MenuResponse;
import shanepark.foodbox.api.service.MenuService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    @GetMapping("/menu/today")
    public ApiResponse getTodayMenu() {
        MenuResponse menu = menuService.getTodayMenu();
        return ApiResponse.success(menu);
    }

    @GetMapping(value = "/menu")
    public ApiResponse getMenu() {
        return ApiResponse.success(menuService.findAll());
    }

}
