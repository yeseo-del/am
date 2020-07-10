package me.rhin.openciv.server.game.production;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import me.rhin.openciv.server.Server;
import me.rhin.openciv.server.game.city.City;
import me.rhin.openciv.server.game.city.building.Building;
import me.rhin.openciv.server.game.city.building.type.Granary;
import me.rhin.openciv.server.game.city.building.type.Monument;
import me.rhin.openciv.server.game.unit.type.Galley;
import me.rhin.openciv.server.game.unit.type.Scout;
import me.rhin.openciv.server.game.unit.type.Settler;
import me.rhin.openciv.server.game.unit.type.Warrior;
import me.rhin.openciv.server.listener.TurnTimeUpdateListener;
import me.rhin.openciv.shared.stat.Stat;

/**
 * Manager that handles the available items to be produced by cities. Producible
 * items are determined by whether the ProductionItem interface meets it's
 * production requirements.
 * 
 * @author Ryan
 *
 */
public class ProducibleItemManager implements TurnTimeUpdateListener {

	private City city;
	private HashMap<String, ProductionItem> possibleItems;

	// Items that the city has already applied production to
	private HashMap<String, ProducingItem> appliedProductionItems;

	// A queue of items to produce
	private Queue<ProducingItem> itemQueue;
	private boolean queueEnabled;

	public ProducibleItemManager(City city) {
		this.city = city;
		this.possibleItems = new HashMap<>();
		this.appliedProductionItems = new HashMap<>();
		this.itemQueue = new LinkedList<>();
		this.queueEnabled = false;

		possibleItems.put("Granary", new Granary(city));
		possibleItems.put("Monument", new Monument(city));
		possibleItems.put("Warrior", new Warrior());
		possibleItems.put("Settler", new Settler());
		possibleItems.put("Scout", new Scout());
		possibleItems.put("Galley", new Galley());

		Server.getInstance().getEventManager().addListener(TurnTimeUpdateListener.class, this);
	}

	public Collection<ProductionItem> getItems() {
		return possibleItems.values();
	}

	public ArrayList<ProductionItem> getProducibleItems() {
		ArrayList<ProductionItem> producibleItems = new ArrayList<>();

		for (ProductionItem item : possibleItems.values()) {
			if (item.meetsProductionRequirements())
				producibleItems.add(item);
		}

		return producibleItems;
	}

	public void setProducingItem(String itemName) {
		if (possibleItems.get(itemName) == null)
			return;

		// Prevent buildings being added twice.
		if (possibleItems.get(itemName) instanceof Building) {
			for (ProducingItem producingItem : itemQueue) {
				if (producingItem.getProductionItem().getName().equals(itemName))
					return;
			}
		}

		System.out.println("Adding production item to queue: " + possibleItems.get(itemName).getName() + " in city: "
				+ city.getName());

		if (queueEnabled)
			itemQueue.add(new ProducingItem(possibleItems.get(itemName)));
		else {

		}
	}

	@Override
	public void onTurnTimeUpdate(int turnTime) {
		ProducingItem producingItem = itemQueue.peek();
		producingItem.applyProduction(city.getStatLine().getStatValue(Stat.PRODUCTION_GAIN));

		if (producingItem.getAppliedProduction() > producingItem.getProductionItem().getProductionCost()) {
			itemQueue.remove();
			itemQueue.peek().applyProduction(
					itemQueue.peek().getAppliedProduction() - producingItem.getProductionItem().getProductionCost());

			// TODO: Send FinishProductionItemPacket
		}
	}
}
