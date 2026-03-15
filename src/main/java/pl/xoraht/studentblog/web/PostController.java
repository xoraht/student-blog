package pl.xoraht.studentblog.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.xoraht.studentblog.domain.Post;
import pl.xoraht.studentblog.dto.PostForm;
import pl.xoraht.studentblog.repo.PostRepository;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Proste: jeśli user zostawi puste, zapisujemy null
    private String cleanUrl(String url) {
        if (url == null) return null;
        url = url.trim();
        return url.isEmpty() ? null : url;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
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
    public String create(@Valid @ModelAttribute("postForm") PostForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "posts/new";
        }

        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setImageUrl(cleanUrl(form.getImageUrl()));

        postRepository.save(post);
        return "redirect:/posts";
    }

    // --- EDYCJA ---

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return "redirect:/posts";
        }

        PostForm form = new PostForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        form.setImageUrl(post.getImageUrl());

        model.addAttribute("post", post);
        model.addAttribute("postForm", form);
        return "posts/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable long id,
                         @Valid @ModelAttribute("postForm") PostForm form,
                         BindingResult bindingResult,
                         Model model) {

        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return "redirect:/posts";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "posts/edit";
        }

        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setImageUrl(cleanUrl(form.getImageUrl()));

        postRepository.save(post);
        return "redirect:/posts/" + post.getId();
    }

    // --- USUWANIE ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        postRepository.findById(id).ifPresent(postRepository::delete);
        return "redirect:/posts";
    }
}