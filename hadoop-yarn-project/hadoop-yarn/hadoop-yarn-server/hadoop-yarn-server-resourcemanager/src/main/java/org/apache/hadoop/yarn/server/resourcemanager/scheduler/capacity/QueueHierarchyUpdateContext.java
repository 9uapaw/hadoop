/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity;

import org.apache.commons.collections.map.LazyMap;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.nodelabels.CommonNodeLabelsManager;
import org.apache.hadoop.yarn.server.resourcemanager.nodelabels.RMNodeLabelsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.hadoop.yarn.nodelabels.CommonNodeLabelsManager.NO_LABEL;

/**
 * A storage that encapsulates intermediate calculation values throughout a
 * full queue update phase.
 */
public class QueueHierarchyUpdateContext {
  private final Resource updatedClusterResource;

  private final Map<String, QueueBranchContext> queueBranchContext
      = LazyMap.decorate(new HashMap<String, QueueBranchContext>(),
      QueueBranchContext::new);
  private final RMNodeLabelsManager labelsManager;

  private List<QueueUpdateWarning> warnings = new ArrayList<QueueUpdateWarning>();

  public QueueHierarchyUpdateContext(Resource updatedClusterResource,
                                     RMNodeLabelsManager labelsManager) {
    this.updatedClusterResource = updatedClusterResource;
    this.labelsManager = labelsManager;
  }

  /**
   * Returns the overall cluster resource available for the update phase.
   * @return cluster resource
   */
  public Resource getUpdatedClusterResource(String label) {
    return labelsManager.getResourceByLabel(label, updatedClusterResource);
  }

  /**
   * Returns the overall cluster resource available for the update phase.
   * @return cluster resource
   */
  public Resource getUpdatedClusterResource() {
    return getUpdatedClusterResource(NO_LABEL);
  }

  /**
   * Returns the context for a queue branch, which is identified by the path of
   * the parent.
   * @param queuePath queue path of the parent
   * @return queue branch context
   */
  public QueueBranchContext getQueueBranchContext(String queuePath) {
    return queueBranchContext.get(queuePath);
  }

  public void addUpdateWarning(QueueUpdateWarning warning) {
    warnings.add(warning);
  }

  public List<QueueUpdateWarning> getUpdateWarnings() {
    return warnings;
  }
}
