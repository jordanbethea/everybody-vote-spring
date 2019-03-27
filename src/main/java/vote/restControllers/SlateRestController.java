package vote.restControllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vote.model.entities.Selection;
import vote.model.entities.Slate;
import vote.model.dataTransferObjects.SlateDTO;
import vote.model.dataTransferObjects.SelectionDTO;
import vote.repositories.BallotRepository;
import vote.repositories.SelectionRepository;
import vote.repositories.SlateRepository;
import org.modelmapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/slate")
public class SlateRestController {
    private static final Logger log = LoggerFactory.getLogger(SlateRestController.class);

    private final SelectionRepository selRepository;
    private final SlateRepository slateRepository;
    private final BallotRepository ballotRepository;

    @Autowired
    public SlateRestController(SelectionRepository selectionRepository,
                           SlateRepository slateRepository, BallotRepository ballotRepository){
        this.selRepository = selectionRepository;
        this.slateRepository = slateRepository;
        this.ballotRepository = ballotRepository;
    }

    @GetMapping()
    public Collection<SlateDTO> showSlates(){
        ModelMapper modelMapper = new ModelMapper();
        Iterable<Slate> slates = slateRepository.findAll();
        List<SlateDTO> toReturnList = new ArrayList<>();
        slates.forEach((Slate a) -> toReturnList.add(modelMapper.map(a, SlateDTO.class)));
        //slates.forEach(toReturnList::add);
        return toReturnList;
    }

    @PostMapping(value = "/new")
    public String saveSlate(@RequestBody SlateDTO newSlateInfo){
log.info("saving new slate");
        ModelMapper modelMapper = new ModelMapper();
        Slate newSlate = modelMapper.map(newSlateInfo, Slate.class);
log.info("new slate info: " + newSlate.toString());
        Slate result = slateRepository.save(newSlate);
log.info("saved result: " + result.toString());
        for(Selection s: newSlate.getSelections()) {
            log.info("Slate has selection: "+s.toString());
            s.setSlate(newSlate);
            selRepository.save(s);
        }
        if(result != null){ return "Successfully Saved"; }
        else { return "Not successfully saved. :("; }
    }

    @GetMapping(value = "/new")
    public SlateDTO newSlate() {
        ModelMapper modelMapper = new ModelMapper();
        Slate newSlate = new Slate();
        newSlate.addSelection(new Selection());
        newSlate.addSelection(new Selection());
        newSlate.addSelection(new Selection());
        return modelMapper.map(newSlate, SlateDTO.class);
    }
}
