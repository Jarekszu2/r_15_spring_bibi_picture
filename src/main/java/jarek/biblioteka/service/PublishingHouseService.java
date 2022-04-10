package jarek.biblioteka.service;

import jarek.biblioteka.model.PublishingHouse;
import jarek.biblioteka.repository.PublishingHouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PublishingHouseService {

    private final PublishingHouseRepository publishingHouseRepository;

    public List<PublishingHouse> getAll() {
        return publishingHouseRepository.findAll();
    }

    public void save(PublishingHouse publishingHouse) {
        publishingHouseRepository.save(publishingHouse);
    }
}
