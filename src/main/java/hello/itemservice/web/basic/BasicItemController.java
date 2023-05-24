package hello.itemservice.web.basic;

import hello.itemservice.domain.Item.Item;
import hello.itemservice.domain.Item.ItemRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    //@Autowired 생성자가 단 하나 있다면, 스프링에서는 자동으로 주입해 준다.
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(
        @PathVariable
        long itemId,
        Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String save(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {

        Item item = new Item();

        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        //ModelAttribute의 name은 무엇인가?
        //model.addAttribute("item", item); 해당 코드를 실행 해 주는 결과를 가지고 있다.

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);

        // ModelAttribute의 name을 지우면?
        // Default 설정은 Item -> item 으로 박아준다 결과론 적으로 클래스의 이름에서 첫 글자를 소문자로 바꿔서 넣어주는 것이다.
        // 따라서 model.addAttribute("item", item); 해당 코드를 실행 해 주는 결과를 가지고 있다.

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {
        // ModelAttribute를 지우면?
        // 역시 Item -> item 으로 치환해서 모델에 가지고 있다. 고로 위에서 작동한 동작과 같다.
        // 주의할 점은 영한 햄은 보다 명시해 주는걸 선호한다.
        itemRepository.save(item);

        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV5(Item item) {
        // ModelAttribute를 지우면?
        // 역시 Item -> item 으로 치환해서 모델에 가지고 있다. 고로 위에서 작동한 동작과 같다.
        // 주의할 점은 영한 햄은 보다 명시해 주는걸 선호한다.
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       @ModelAttribute Item item) {

        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

    }
}
