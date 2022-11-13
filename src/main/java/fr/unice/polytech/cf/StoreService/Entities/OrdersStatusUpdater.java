package fr.unice.polytech.cf.StoreService.Entities;

import fr.unice.polytech.cf.OrderService.Entities.Order;
import fr.unice.polytech.cf.OrderService.Enums.EOrderStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdersStatusUpdater {

    public final List<Order> orders;

    public OrdersStatusUpdater(List<Order> orders) {
        this.orders = orders;
    }

    public void updatePreparedOrdersStatus(Date currentDate) {
        for ( Order order : orders ) {
            if ( order.getStatus().equals(EOrderStatus.PREPARED) )
                if ( howLongItsBeenFromRetrievalDateUntilCurrentDate(currentDate, order.getRetrievalDateTime()) >= 120 )
                    order.setStatus(EOrderStatus.OBSOLETE);
        }
    }

    public long howLongItsBeenFromRetrievalDateUntilCurrentDate(Date currentDate, Date orderRetrievalDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(orderRetrievalDate);
        c.add(Calendar.YEAR, 52);
        orderRetrievalDate = c.getTime();
        c.setTime(currentDate);
        c.add(Calendar.YEAR, 52);
        currentDate = c.getTime();
        long differenceInMinutes = currentDate.getTime() - orderRetrievalDate.getTime();
        // Convert the difference into minutes
        return differenceInMinutes / 60000;
    }

}
