package ru.milkov.credit_calc.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import ru.milkov.credit_calc.ui.view.bankInfo.BankInfoView;
import ru.milkov.credit_calc.ui.view.bankList.BankListView;
import ru.milkov.credit_calc.ui.view.creditOffer.CreditOfferCalcView;
import ru.milkov.credit_calc.ui.view.creditOfferList.CreditOfferListView;


@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Credit Calc App");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink bankInfo = new RouterLink("Bank Info", BankInfoView.class);
        bankInfo.setHighlightCondition(HighlightConditions.sameLocation());


        RouterLink creditOfferCalcView = new RouterLink("Credit Offer Calc", CreditOfferCalcView.class);
        creditOfferCalcView.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink bankList = new RouterLink("Bank List", BankListView.class);
        bankList.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink creditOfferList = new RouterLink("Credit Offer List", CreditOfferListView.class);
        creditOfferList.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(bankInfo, creditOfferCalcView, bankList, creditOfferList));
    }
}

