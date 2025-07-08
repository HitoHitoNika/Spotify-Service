package de.hitohitonika.spotify.debtservice.boundary;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.hitohitonika.spotify.debtservice.control.PaymentInfoService;
import de.hitohitonika.spotify.debtservice.entity.PaymentInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route("debts")
@PageTitle("Spotify Übersicht")
public class DebtView extends AppLayout implements BeforeEnterObserver {
    private final static List<String> CAT_MEDIA_LINKS = List.of(
            "https://media1.tenor.com/m/v7KCJld0oo0AAAAC/choy-cat.gif",
            "https://media1.tenor.com/m/ktQmSQl3GvoAAAAd/kataman-cat.gif",
            "https://media1.tenor.com/m/KfPoZmalV00AAAAd/cuh-cuh-cat.gif",
            "https://media1.tenor.com/m/tmK6vWeDw6AAAAAd/catsena.gif",
            "https://media1.tenor.com/m/LZjUkhWST2cAAAAd/4evil-jabroni-mike.gif",
            "https://media1.tenor.com/m/v7KCJld0oo0AAAAC/choy-cat.gif",
            "https://media1.tenor.com/m/o_5RQarGvJ0AAAAd/kiss.gif",
            "https://media1.tenor.com/m/F-XdiaIlslsAAAAd/orange-cat.gif",
            "https://media1.tenor.com/m/eEImeJOo-58AAAAd/cat-laughing-meme.gif",
            "https://media1.tenor.com/m/i6HUfyNrvNYAAAAd/larry-larry-cat.gif",
            "https://media1.tenor.com/m/zaulJgJ3xPQAAAAd/cat-flash.gif",
            "https://media1.tenor.com/m/v7KCJld0oo0AAAAC/choy-cat.gif",
            "https://media1.tenor.com/m/BuImo5Z739UAAAAd/cat.gif",
            "https://media1.tenor.com/m/yL-hnD0X5eIAAAAd/buh.gif",
            "https://media1.tenor.com/m/zGQLL-kwwEoAAAAd/cat-meme-pee.gif",
            "https://media1.tenor.com/m/TV_GLJrCn2oAAAAd/uuh-cat.gif",
            "https://media1.tenor.com/m/v7KCJld0oo0AAAAC/choy-cat.gif",
            "https://media1.tenor.com/m/oCqm_-5NBaEAAAAd/aa-uh-uh-uh.gif",
            "https://media1.tenor.com/m/gmYV0hxLe6YAAAAC/kedinehir-nehirkedi.gif",
            "https://media1.tenor.com/m/huz2P7A-GtsAAAAC/cat-oi.gif",
            "https://media1.tenor.com/m/v7KCJld0oo0AAAAC/choy-cat.gif"
    );


    private final Grid<PaymentInfo> grid = new Grid<>(PaymentInfo.class, false);
    private final ListDataProvider<PaymentInfo> dataProvider;
    private final List<String> userNames;
    private final ComboBox<String> userFilter = new ComboBox<>("Anzeigen für:");




    public DebtView(PaymentInfoService paymentInfoService) {
        List<PaymentInfo> allPayments = paymentInfoService.getAllPaymentInfos();
        this.dataProvider = new ListDataProvider<>(allPayments);
        this.userNames = allPayments.stream()
                .map(PaymentInfo::getName)
                .distinct()
                .collect(Collectors.toList());

        H1 title = new H1("Spotify Übersicht");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        addToNavbar(title);

        VerticalLayout content = createContent();
        setContent(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        List<String> namesFromQuery = parametersMap.get("name");

        if (namesFromQuery != null && !namesFromQuery.isEmpty()) {
            String nameToFilter = namesFromQuery.get(0);

            if (this.userNames.contains(nameToFilter)) {
                dataProvider.setFilter(info -> info.getName().equals(nameToFilter));
                userFilter.setValue(nameToFilter);
            }
        }
    }

    private VerticalLayout createContent() {
        userFilter.setItems(userNames);
        userFilter.setPlaceholder("Alle anzeigen");
        userFilter.setClearButtonVisible(true);
        userFilter.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                dataProvider.clearFilters();
            } else {
                dataProvider.setFilter(info -> info.getName().equals(event.getValue()));
            }
        });

        grid.setDataProvider(dataProvider);
        grid.addColumn(PaymentInfo::getName).setHeader("Person").setSortable(true);
        grid.addComponentColumn(info -> new Span(info.getDebt() + " €")).setHeader("Offener Betrag").setSortable(true)
                .setClassNameGenerator(info -> "center-align");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setAllRowsVisible(true);

        FlexLayout gifContainer = new FlexLayout();
        gifContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        gifContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        gifContainer.addClassName(LumoUtility.Gap.MEDIUM);

        for (String url : CAT_MEDIA_LINKS) {
            Image gif = new Image(url, "Fun GIF");
            gif.setWidth("220px");
            gif.setHeight("220px");
            gif.getStyle().set("object-fit", "cover");
            gif.getStyle().set("border-radius", "var(--lumo-border-radius-l)");
            gifContainer.add(gif);
        }

        VerticalLayout contentLayout = new VerticalLayout(userFilter, grid, gifContainer);
        contentLayout.setPadding(true);
        contentLayout.setAlignItems(VerticalLayout.Alignment.CENTER);
        return contentLayout;
    }

}