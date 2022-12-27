package ua.goit.dev6.note;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ua.goit.dev6.account.UserDAO;
import ua.goit.dev6.account.UserDTO;
import ua.goit.dev6.account.UserService;
import ua.goit.dev6.config.UserPrincipal;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;


    @GetMapping("/findAll")
    private ModelAndView findAll(){
        ModelAndView result = new ModelAndView("/notes/notes");
        List<NoteDTO> notes = noteService.findAll();
        result.addObject("notes", notes);
        return result;
    }

    @GetMapping("/create")
    private ModelAndView getCreateForm() {
        ModelAndView result = new ModelAndView("/notes/createNoteForm");
        NoteDTO noteDTO = new NoteDTO();
        result.addObject("note", noteDTO);
        return result;
    }

    @GetMapping("/edit")
    private ModelAndView getEditForm(@RequestParam("id") String id) {
        ModelAndView result = new ModelAndView("/notes/editNoteForm");
        NoteDTO editNote = noteService.findById(UUID.fromString(id));
        result.addObject("note", editNote);
        return result;
    }

    @PostMapping("/save")
    private RedirectView save(@Validated @ModelAttribute("note") NoteDTO note){
        note.setUser(userService.getAuthorizedUser());
        noteService.save(note);
        return new RedirectView("/notes/findAll");
    }

    @GetMapping("/delete")
    private RedirectView delete(@RequestParam("id") String id) {
        noteService.deleteById(UUID.fromString(id));
        return new RedirectView("/notes/findAll");
    }

}