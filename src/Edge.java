/**
 * Copyright © 2021 Kasper Krawczyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class Edge {
    private int cost;
    private Cell destination;

    public Edge(int cost, Cell destination){
        this.cost = cost;
        this.destination = destination;
    }

    public int getCost(){
        return cost;
    }

    public Cell getDestination(){
        return destination;
    }

    public void setCost(int cost){
        this.cost = cost;
    }
}
