package ru.milkov.credit_calc.ui.view.creditOfferList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.milkov.credit_calc.backend.domain.CreditOffer;
import ru.milkov.credit_calc.backend.domain.Payment;
import ru.milkov.credit_calc.backend.service.CreditOfferService;
import ru.milkov.credit_calc.ui.MainLayout;

@Route(value = "creditOfferListView", layout = MainLayout.class)
@PageTitle("Credit Offer List | Credit Calc")
public class CreditOfferListView extends VerticalLayout {
    CreditOfferService creditOfferService;

    Select<CreditOffer> creditOfferSelect = new Select<>();
    private TextField creditAmmountTF = new TextField();
    private TextField creditPercentsTF = new TextField();
    Button delete = new Button("Delete");
    VerticalLayout toolbar = new VerticalLayout();
    Grid<Payment> paymentSheduleGrid = new Grid<>(Payment.class);

    CreditOffer creditOffer;

    public CreditOfferListView(CreditOfferService creditOfferService) {
        this.creditOfferService = creditOfferService;

        confToolbar();
        confPaymentSheduleGrid();
        delete.setEnabled(false);
        add(toolbar, paymentSheduleGrid);
        updatePaymentShedule();
    }

    private void confToolbar(){
        updateToolbar();
        creditOfferSelect.setItemLabelGenerator(CreditOffer::getInfo);
        creditOfferSelect.addValueChangeListener(event -> setCreditOffer(event.getValue()));
        creditOfferSelect.setLabel("choose credit offer");
        delete.setEnabled(false);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addClickListener(click -> deleteCreditOffer());
        creditAmmountTF.setReadOnly(true);
        creditAmmountTF.setLabel("Credit Amount");
        creditPercentsTF.setReadOnly(true);
        creditPercentsTF.setLabel("Percent Amount");
        HorizontalLayout firstLine = new HorizontalLayout(creditOfferSelect, delete);
        firstLine.setDefaultVerticalComponentAlignment(Alignment.END);
        toolbar.add(firstLine,
                new HorizontalLayout(creditAmmountTF, creditPercentsTF));
    }

    private void confPaymentSheduleGrid() {
        paymentSheduleGrid.setColumns("date", "payment", "creditPercent", "creditBody", "remainder");
        paymentSheduleGrid.removeColumnByKey("payment");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getPayment()))
                .setHeader("Payment");
        paymentSheduleGrid.removeColumnByKey("creditPercent");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getCreditPercent()))
                .setHeader("Percents");
        paymentSheduleGrid.removeColumnByKey("creditBody");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getCreditBody()))
                .setHeader("Credit Body");
        paymentSheduleGrid.removeColumnByKey("remainder");
        paymentSheduleGrid.addColumn(payment -> String.format("%.2f", payment.getRemainder()))
                .setHeader("Remainder");
        paymentSheduleGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        for (Grid.Column c :
                paymentSheduleGrid.getColumns()) {
            c.setSortable(false);
        }
    }

    public void setCreditOffer(CreditOffer creditOffer) {
        this.creditOffer = creditOffer;
        delete.setEnabled(true);
        updatePaymentShedule();
    }

    public void deleteCreditOffer() {
        creditOfferService.delete(creditOffer);
        creditOffer = null;

        updatePaymentShedule();
        updateToolbar();
    }

    public void updatePaymentShedule(){
       if (creditOffer != null){
           paymentSheduleGrid.setVisible(true);
           paymentSheduleGrid.setItems(creditOffer.getPaymentShedule());
           creditAmmountTF.setValue(String.format("%.2f %s",
                   creditOffer.getCreditAmmount(),
                   creditOffer.getCredit().getCurrency()));
           creditPercentsTF.setValue(String.format("%.2f %s",
                   creditOffer.getCreditPercents(),
                   creditOffer.getCredit().getCurrency()));
       } else paymentSheduleGrid.setVisible(false);
    }
    private void updateToolbar() {
        creditOfferSelect.setItems(creditOfferService.findAll());
        creditOfferSelect.clear();
        creditPercentsTF.clear();
        creditAmmountTF.clear();
    }
}