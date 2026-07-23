package ru.ryzhukvlad.controller.secured;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ryzhukvlad.entity.RecordStatus;
import ru.ryzhukvlad.entity.dto.RecordsContainerDto;
import ru.ryzhukvlad.service.RecordService;

@Controller
@RequestMapping("/account")
public class PrivateAccountController {
    private final RecordService recordService;

    @Autowired
    public PrivateAccountController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public String getMainPage(HttpServletRequest request, Model model, @RequestParam(name = "filter", required = false) String filterMode) {
        HttpSession session = request.getSession();
        Object counter = session.getAttribute("visitsCounter");
        if (counter != null) {
            model.addAttribute("visitsCounter", counter);
            session.setAttribute("visitsCounter", ((Integer) counter + 1));
        } else {
            model.addAttribute("visitsCounter", 0);
            session.setAttribute("visitsCounter", 1);
        }
        RecordsContainerDto container = recordService.findAllRecords(filterMode);
        model.addAttribute("userName", container.getUserName());
        model.addAttribute("records", container.getRecords());
        model.addAttribute("numberOfDoneRecords", container.getNumberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.getNumberOfActiveRecords());
        return "private/account-page";
    }

    @PostMapping("/add-record")
    public String addRecord(@RequestParam("title") String title) {
        recordService.saveRecord(title);
        return "redirect:/account";
    }

    @PostMapping("/make-record-done")
    public String makeRecordDone(@RequestParam("id") int id,
                                 @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.updateRecordStatus(id, RecordStatus.DONE);
        return "redirect:/account" + (filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }

    @PostMapping("/delete-record")
    public String deleteRecord(@RequestParam("id") int id,
                               @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.deleteRecord(id);
        return "redirect:/account" + (filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }
}
