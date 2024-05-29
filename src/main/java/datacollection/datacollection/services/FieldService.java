package datacollection.datacollection.services;

import datacollection.datacollection.dtos.FieldDTO;
import datacollection.datacollection.repositories.FieldRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;

    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public void updateFieldSequence(FieldDTO field, int newSequence) {
        int oldSequence = field.getSequence();

        if (newSequence != oldSequence) {
            // Determine the range and direction of the sequence update
            int min = Math.min(oldSequence, newSequence);
            int max = Math.max(oldSequence, newSequence);

            // Fetch fields within the range that need updating
            fieldRepository.findBySectionIdAndSequenceBetween(field.getSectionId(), min, max).forEach(f -> {
                if (f.getId().equals(field.getId())) {
                    f.setSequence(newSequence);
                    fieldRepository.updateFieldSequence(f.getId(), newSequence);
                } else if (newSequence < oldSequence) {
                    if (f.getSequence() >= newSequence && f.getSequence() < oldSequence) {
                        f.setSequence(f.getSequence() + 1);
                        fieldRepository.updateFieldSequence(f.getId(), f.getSequence());
                    }
                } else {
                    if (f.getSequence() > oldSequence && f.getSequence() <= newSequence) {
                        f.setSequence(f.getSequence() - 1);
                        fieldRepository.updateFieldSequence(f.getId(), f.getSequence());
                    }
                }
            });
        }
    }
}
