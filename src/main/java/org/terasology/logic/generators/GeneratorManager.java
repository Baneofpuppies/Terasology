/*
 * Copyright 2011 Benjamin Glatzel <benjamin.glatzel@me.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.logic.generators;

 import org.terasology.logic.world.LocalWorldProvider;
 import org.terasology.utilities.FastRandom;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Benjamin Glatzel <benjamin.glatzel@me.com>
 */
public class GeneratorManager {

    private final LocalWorldProvider _parent;
    // TODO: Improve random handling
    private FastRandom random;

    /* WORLD GENERATION */
    protected final ArrayList<ChunkGenerator> _chunkGenerators = new ArrayList<ChunkGenerator>(8);
    protected final HashMap<ChunkGeneratorTerrain.BIOME_TYPE, ArrayList<TreeGenerator>> _treeGenerators = new HashMap<ChunkGeneratorTerrain.BIOME_TYPE, ArrayList<TreeGenerator>>(8);

    public GeneratorManager(LocalWorldProvider parent) {
        _parent = parent;
        random = new FastRandom(parent.getSeed().hashCode());


        // Init. static generators
        _chunkGenerators.add(new ChunkGeneratorTerrain(this));
        _chunkGenerators.add(new ChunkGeneratorFlora(this, parent.getSeed().hashCode()));
        _chunkGenerators.add(new ChunkGeneratorLiquids(this, random));

        loadTrees();
    }

    public void loadTrees() {
        // TODO: Turn this into a generic tree loader that goes looking for any tree definitions (MetaBlock = Tree ?)
        new DefaultGenerators(this);
    }

    public ArrayList<TreeGenerator> getTreeGenerators(ChunkGeneratorTerrain.BIOME_TYPE type) {
        return _treeGenerators.get(type);
    }

    public ArrayList<ChunkGenerator> getChunkGenerators() {
        return _chunkGenerators;
    }

    public void addTreeGenerator(ChunkGeneratorTerrain.BIOME_TYPE type, TreeGenerator gen) {
        ArrayList<TreeGenerator> list;

        if (!_treeGenerators.containsKey(type)) {
            list = new ArrayList<TreeGenerator>();
            _treeGenerators.put(type, list);
        } else {
            list = _treeGenerators.get(type);
        }

        list.add(gen);
    }

    public void removeTreeGenerator(ChunkGeneratorTerrain.BIOME_TYPE type, TreeGenerator gen) {
        ArrayList<TreeGenerator> list = _treeGenerators.get(type);

        if (list == null)
            return;

        list.remove(gen);
    }

    public void removeTreeGenerator(ChunkGeneratorTerrain.BIOME_TYPE type, int id) {
        ArrayList<TreeGenerator> list = _treeGenerators.get(type);

        if (list == null)
            return;

        list.remove(id);
    }

    public TreeGenerator getTreeGenerator(ChunkGeneratorTerrain.BIOME_TYPE type, int id) {
        ArrayList<TreeGenerator> list = _treeGenerators.get(type);

        if (list == null)
            return null;

        return list.get(id);
    }

    public LocalWorldProvider getParent() {
        return _parent;
    }

}