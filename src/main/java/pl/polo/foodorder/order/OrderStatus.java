package pl.polo.foodorder.order;

public enum OrderStatus {
    NEW,
    IN_PROGRESS,
    COMPLETE;

    static OrderStatus nextStatus(OrderStatus status){
        if(status == NEW)
            return IN_PROGRESS;
        else if(status == IN_PROGRESS)
            return COMPLETE;
        else
            throw new IllegalArgumentException(("Nie ma następnego statusu dla tej wartości"));
    }

}
