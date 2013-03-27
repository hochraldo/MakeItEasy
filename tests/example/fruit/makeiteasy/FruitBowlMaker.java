package example.fruit.makeiteasy;

import com.makeiteasy.Instantiator;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

import example.fruit.Fruit;
import example.fruit.FruitBowl;

import static com.makeiteasy.MakeItEasy.*;
import static com.makeiteasy.Property.newProperty;
import static example.fruit.makeiteasy.FruitMakers.Apple;
import static example.fruit.makeiteasy.FruitMakers.Banana;


/**
 * An example of how to define builders for properties that are collections.
 */
public class FruitBowlMaker {
    public static final Property<FruitBowl, Iterable<? extends Fruit>> contents = newProperty();

    public static final Instantiator<FruitBowl> FruitBowl = new Instantiator<FruitBowl>() {
        @Override
        public FruitBowl instantiate(PropertyLookup<FruitBowl> lookup) {
            return new FruitBowl(
                    lookup.valueOf(contents, listOf(an(Apple), a(Banana)).value()));
        }
    };
}