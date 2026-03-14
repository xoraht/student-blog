package pl.xoraht.studentblog.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.xoraht.studentblog.dto.PostForm;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    // Na razie “pamięciowa baza”, żeby mieć widoki i endpointy.
    private final List<SimplePost> posts = new ArrayList<>(List.of(
            new SimplePost(1L, "Pierwszy post", "To jest przykładowa treść pierwszego posta."),
            new SimplePost(2L, "Drugi post", "To jest przykładowa treść drugiego posta.")
    ));

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable long id, Model model) {
        SimplePost post = posts.stream()
                .filter(p -> p.id() == id)
                .findFirst()
                .orElse(null);

        if (post == null) {
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        return "posts/details";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "posts/new";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("postForm") PostForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "posts/new";
        }

        long nextId = posts.stream().mapToLong(SimplePost::id).max().orElse(0L) + 1L;
        posts.add(new SimplePost(nextId, form.getTitle(), form.getContent()));

        return "redirect:/posts";
    }

    // Prosty rekord do widoków (bez JPA na razie)
    public record SimplePost(long id, String title, String content) {}
}