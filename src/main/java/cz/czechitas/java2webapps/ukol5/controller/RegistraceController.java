package cz.czechitas.java2webapps.ukol5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;

/**
 * Kontroler obsluhující registraci účastníků dětského tábora.
 */
@Controller()
@RequestMapping("/")
public class RegistraceController {

    @GetMapping("")
    public ModelAndView nactiFormular() {
        return new ModelAndView("formular").addObject("form", new RegistraceForm());
    }

    @PostMapping("")
    // zpracování dat z formuláře a základní kontrola povinných údajů
    public Object form(@ModelAttribute("form") @Valid RegistraceForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }

        //výpočet věku dítěte z data narození
        Period period = form.getDatumNarozeni().until(LocalDate.now());
        int vek = period.getYears();

        // Validace věku dítěte dle zadání (9-15 let vč.), vypsání chybové hlášky přímo ve formuláři
        if ((vek < 9) || (vek > 15)) {
            bindingResult.rejectValue("datumNarozeni", "", "Věk musí být v rozsahu 9 - 15 let (včetně).");
            return "formular";
        }

        // Validace zaškrtnutí min. 2 sportů, vypsání chybové hlášky přímo ve formuláři
        if ((form.getSport() == null) || (form.getSport().size() < 2)) {
            bindingResult.rejectValue("sport", "", "Musíte vyplnit min. 2 sporty.");
            return "formular";
        }

        // po kontrole - vše je správně, přechod na stránku s rekapitulací
        return "rekapitulace";
    }
}