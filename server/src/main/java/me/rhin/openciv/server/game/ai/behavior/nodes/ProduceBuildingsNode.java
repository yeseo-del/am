package me.rhin.openciv.server.game.ai.behavior.nodes;

import me.rhin.openciv.server.game.ai.behavior.BehaviorStatus;
import me.rhin.openciv.server.game.ai.behavior.CityNode;
import me.rhin.openciv.server.game.city.City;
import me.rhin.openciv.server.game.city.building.Building;
import me.rhin.openciv.server.game.production.ProducibleItemManager;
import me.rhin.openciv.server.game.production.ProductionItem;

public class ProduceBuildingsNode extends CityNode {

	public ProduceBuildingsNode(City city, String name) {
		super(city, name);
	}

	@Override
	public void tick() {

		ProducibleItemManager itemManager = city.getProducibleItemManager();

		ProductionItem topItem = null;

		for (ProductionItem productionItem : itemManager.getProducibleItems()) {

			if (!(productionItem instanceof Building))
				continue;

			Building building = (Building) productionItem;

			if (city.containsBuilding(building.getClass()))
				continue;

			if (topItem == null || topItem.getAIValue() < building.getAIValue())
				topItem = building;
		}

		if (topItem == null) {
			setStatus(BehaviorStatus.FAILURE);
			return;
		}

		itemManager.setProducingItem(topItem.getName());
		setStatus(BehaviorStatus.SUCCESS); 
	}

}