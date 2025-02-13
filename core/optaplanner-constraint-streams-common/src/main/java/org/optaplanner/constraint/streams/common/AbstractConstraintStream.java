package org.optaplanner.constraint.streams.common;

import java.util.Objects;
import java.util.function.Function;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfigurationProvider;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.stream.ConstraintStream;
import org.optaplanner.core.impl.domain.constraintweight.descriptor.ConstraintConfigurationDescriptor;
import org.optaplanner.core.impl.domain.constraintweight.descriptor.ConstraintWeightDescriptor;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;

public abstract class AbstractConstraintStream<Solution_> implements ConstraintStream {

    private final RetrievalSemantics retrievalSemantics;

    protected AbstractConstraintStream(RetrievalSemantics retrievalSemantics) {
        this.retrievalSemantics = Objects.requireNonNull(retrievalSemantics);
    }

    public RetrievalSemantics getRetrievalSemantics() {
        return retrievalSemantics;
    }

    // ************************************************************************
    // Penalize/reward
    // ************************************************************************

    protected Function<Solution_, Score<?>> buildConstraintWeightExtractor(String constraintPackage, String constraintName) {
        validateConstraintId(constraintPackage, constraintName);
        SolutionDescriptor<Solution_> solutionDescriptor = getConstraintFactory().getSolutionDescriptor();
        ConstraintConfigurationDescriptor<Solution_> configurationDescriptor = solutionDescriptor
                .getConstraintConfigurationDescriptor();
        if (configurationDescriptor == null) {
            throw new IllegalStateException("The constraint (" + constraintName + ") of package (" + constraintPackage
                    + ") does not hard-code a constraint weight"
                    + " and there is no @" + ConstraintConfigurationProvider.class.getSimpleName()
                    + " on the solution class (" + solutionDescriptor.getSolutionClass() + ").\n"
                    + "Maybe add a @" + ConstraintConfiguration.class.getSimpleName() + " class"
                    + " or use " + ConstraintStream.class.getSimpleName() + ".penalize()/reward()"
                    + " instead of penalizeConfigurable()/rewardConfigurable.");
        }
        ConstraintWeightDescriptor<Solution_> weightDescriptor = configurationDescriptor
                .findConstraintWeightDescriptor(constraintPackage, constraintName);
        if (weightDescriptor == null) {
            throw new IllegalStateException("The constraint (" + constraintName + ") of package (" + constraintPackage
                    + ") does not hard-code a constraint weight"
                    + " and there is no such @" + ConstraintWeight.class.getSimpleName()
                    + " on the constraintConfigurationClass (" + configurationDescriptor.getConstraintConfigurationClass()
                    + ").\n"
                    + "Maybe there is a typo in the constraintPackage or constraintName of one of the @"
                    + ConstraintWeight.class.getSimpleName() + " members.\n"
                    + "Maybe add a @" + ConstraintWeight.class.getSimpleName() + " member for it.");
        }
        return weightDescriptor.createExtractor();
    }

    protected Function<Solution_, Score<?>> buildConstraintWeightExtractor(String constraintPackage, String constraintName,
            Score<?> constraintWeight) {
        validateConstraintId(constraintPackage, constraintName);
        // Duplicates validation when the session is built, but this fails fast when weights are hard coded
        getConstraintFactory().getSolutionDescriptor().validateConstraintWeight(constraintPackage, constraintName,
                constraintWeight);
        return solution -> constraintWeight;
    }

    private static void validateConstraintId(String constraintPackage, String constraintName) {
        if (constraintPackage == null) {
            throw new IllegalStateException("The constraint (" + constraintName
                    + ") cannot have a null package (" + constraintPackage + ").");
        }
        if (constraintName == null) {
            throw new IllegalStateException("The constraint of package (" + constraintPackage
                    + ") cannot have a null name (" + constraintName + ").");
        }
    }

    // ************************************************************************
    // Getters/setters
    // ************************************************************************

    @Override
    public abstract InnerConstraintFactory<Solution_, ?> getConstraintFactory();

}
