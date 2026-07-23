package ru.ryzhukvlad.controller.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    private static final String COMMON_ERROR_PAGE = "public/error/common-error-page";
    private static final String FORBIDDEN_ERROR_PAGE = "public/error/forbidden-error-page";
    private static final String NOT_FOUND_ERROR_PAGE = "public/error/not-found-error-page";

    @RequestMapping("/error")
    public String getSpecificErrorPage(HttpServletRequest request) {
        return switch (getErrorStatus(request)) {
            case FORBIDDEN -> FORBIDDEN_ERROR_PAGE;
            case NOT_FOUND -> NOT_FOUND_ERROR_PAGE;
            case null, default -> COMMON_ERROR_PAGE;
        };
    }

    @RequestMapping("/error/500")
    public String getCommonErrorPage() {
        return COMMON_ERROR_PAGE;
    }

    @RequestMapping("/error/403")
    public String getForbiddenErrorPage() {
        return FORBIDDEN_ERROR_PAGE;
    }

    @RequestMapping("/error/404")
    public String getNotFoundErrorPage() {
        return NOT_FOUND_ERROR_PAGE;
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound() {
        return NOT_FOUND_ERROR_PAGE;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleThrowable(Throwable throwable) {
        return COMMON_ERROR_PAGE;
    }

    private HttpStatus getErrorStatus(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode instanceof Integer code) {
            return HttpStatus.resolve(code);
        }

        return null;
    }
}
