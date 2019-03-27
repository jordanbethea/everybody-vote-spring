package vote.formatters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import vote.model.Selection;
import java.util.Locale;
import vote.repositories.SelectionRepository;

@Component
public class SelectionFormatter implements Formatter<Selection>{

    @Autowired
    private SelectionRepository selectionRepository;

    public Selection parse(String s, Locale locale) {
        Long id = new Long(s);
        return selectionRepository.findById(id).orElse(null);
    }

    public String print(Selection sel, Locale locale) {
        return sel.getId().toString();
    }
}
