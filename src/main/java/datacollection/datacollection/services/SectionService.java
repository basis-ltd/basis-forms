package datacollection.datacollection.services;

import datacollection.datacollection.dtos.SectionDTO;
import datacollection.datacollection.repositories.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void updateSectionSequence(SectionDTO section, int newSequence) {
        int oldSequence = section.getSequence();

        if (newSequence != oldSequence) {
            // Determine the range and direction of the sequence update
            int min = Math.min(oldSequence, newSequence);
            int max = Math.max(oldSequence, newSequence);

            // Fetch sections within the range that need updating
            List<SectionDTO> sectionsToUpdate = sectionRepository.findByFormIdAndSequenceBetween(section.getFormId(), min, max);

            // Update the sequences
            sectionsToUpdate.forEach(s -> {
                if (s.getId().equals(section.getId())) {
                    s.setSequence(newSequence);
                    sectionRepository.updateSectionSequence(s.getId(), newSequence);
                } else if (newSequence < oldSequence) {
                    if (s.getSequence() >= newSequence && s.getSequence() < oldSequence) {
                        s.setSequence(s.getSequence() + 1);
                        sectionRepository.updateSectionSequence(s.getId(), s.getSequence());
                    }
                } else {
                    if (s.getSequence() > oldSequence && s.getSequence() <= newSequence) {
                        s.setSequence(s.getSequence() - 1);
                        sectionRepository.updateSectionSequence(s.getId(), s.getSequence());
                    }
                }
            });
        }
    }

}