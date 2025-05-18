package br.com.Letterbook.Letterbook.controller;

import br.com.Letterbook.Letterbook.model.Users;
import br.com.Letterbook.Letterbook.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class EditUsers {

    @Autowired
    private UsersService usersService;

    @GetMapping("/editUsers")
    public String showEditProfile(HttpSession session, Model model) {

        Users user = (Users) session.getAttribute("usuarioLogado");

        if (user == null) {
            return "redirect:/loginUsers";
        }

        model.addAttribute("user", user);

        return "books/editUsers";
    }

    @PostMapping("/editUsers")
    public String updateProfile(@ModelAttribute Users updatedUser, HttpSession session) {
        Users user = (Users) session.getAttribute("usuarioLogado");

        if (user == null) {
            return "redirect:/loginUsers";
        }

        updatedUser.setCpf(user.getCpf());

        usersService.updateUser(updatedUser);

        session.setAttribute("usuarioLogado", updatedUser);

        return "redirect:/books";
    }
}
