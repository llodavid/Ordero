package be.llodavid.api.itemApi;

import be.llodavid.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;
    private ItemMapper itemMapper;

    @Inject
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        itemService.injectDefaultData();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO createItem (@RequestBody ItemDTO item) {
        return itemMapper.itemToDTO(
                itemService.addItem(
                        itemMapper.dtoToItem(item)));
    }

    @GetMapping(path = "/{itemId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItem(@PathVariable int itemId) {
        return itemMapper.itemToDTO(itemService.getItem(itemId));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getAllItems(){
        return itemService.getAllItems()
                .stream()
                .map(item->itemMapper.itemToDTO(item))
                .collect(Collectors.toList());
    }

    @PutMapping(path = "/{itemId}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItem ( @PathVariable int itemId, @RequestBody ItemDTO item) {
        return itemMapper.itemToDTO(
                itemService.updateItem(
                        itemMapper.dtoToItem(item), itemId));
    }
}
