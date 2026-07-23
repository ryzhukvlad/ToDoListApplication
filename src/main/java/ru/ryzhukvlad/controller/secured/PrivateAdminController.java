package ru.ryzhukvlad.controller.secured;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.ryzhukvlad.entity.User;
import ru.ryzhukvlad.service.UserService;

@Controller
public class PrivateAdminController {
    private final UserService userService;

    public PrivateAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getManagementPage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("userName", user.getName());
        return "private/admin/management-page";
    }
}
