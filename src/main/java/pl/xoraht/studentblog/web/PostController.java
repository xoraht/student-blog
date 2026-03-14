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

        Post post = new Post()
                .setTitle(form.getTitle())
                .setContent(form.getContent());

        postRepository.save(post);

        return "redirect:/posts";
    }
}