package ru.ryzhukvlad.controller.secured;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ryzhukvlad.entity.User;
import ru.ryzhukvlad.entity.UserRole;
import ru.ryzhukvlad.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/super-admin")
public class PrivateSuperAdminController {
    private final UserService userService;

    public PrivateSuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/make-user-admin")
    public String makeUserAdmin(@RequestParam int id) {
        Optional<User> userToBeUpgradedOptional = userService.findById(id);

        if (userToBeUpgradedOptional.isEmpty()) {
            return "redirect:/admin";
        }
        User userToBeUpgraded = userToBeUpgradedOptional.get();
        if (!userToBeUpgraded.isSimpleUser()) {
            return "redirect:/admin";
        }
        userToBeUpgraded.setRole(UserRole.ADMIN);
        userService.save(userToBeUpgraded);

        return "redirect:/admin";
    }
}
