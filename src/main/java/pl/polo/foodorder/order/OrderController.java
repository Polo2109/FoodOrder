package pl.polo.foodorder.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.polo.foodorder.common.Message;
import pl.polo.foodorder.item.Item;
import pl.polo.foodorder.item.ItemRepository;

import java.util.Optional;

@Controller
public class OrderController {

    private ClientOrder clientOrder;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OrderController(ClientOrder clientOrder, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.clientOrder = clientOrder;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }
    @GetMapping("/zamowienie/dodaj")
    public String addItemToOrder(@RequestParam Long itemId, Model model){
        Optional<Item> item = itemRepository.findById(itemId);
        item.ifPresent(clientOrder::add);
        if(item.isPresent()){
            model.addAttribute("message", new Message("Dodano", "Do zamówienia dodano: " + item.get().getName()));
        }else {
            model.addAttribute("message", new Message("Nie dodano", "Pobrano błędne id z menu: " + itemId));
        }
        return "message";
    }
    @GetMapping("/zamowienie")
    public String getCurrentOrder(Model model){
        model.addAttribute("order", clientOrder.getOrder());
        model.addAttribute("sum", clientOrder
                .getOrder()
                .getItems().stream()
                .mapToDouble(Item::getPrice)
                .sum());
        return "order";
    }
    @PostMapping("/zamowienie/realizuj")
    public String proceedOrder(@RequestParam String address, @RequestParam String telephone, Model model){
        Order order = clientOrder.getOrder();
        order.setAddress(address);
        order.setTelephone(telephone);
        orderRepository.save(order);
        clientOrder.clear();
        model.addAttribute("message", new Message("Dziękujemy", "Zamówienie przekazane do realizacji"));
        return "message";
    }
}
