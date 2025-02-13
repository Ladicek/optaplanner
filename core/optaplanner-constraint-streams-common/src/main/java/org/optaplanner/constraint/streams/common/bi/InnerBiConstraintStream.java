package org.optaplanner.constraint.streams.common.bi;

import static org.optaplanner.constraint.streams.common.RetrievalSemantics.STANDARD;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

import org.optaplanner.constraint.streams.common.RetrievalSemantics;
import org.optaplanner.constraint.streams.common.ScoreImpactType;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.bi.BiConstraintBuilder;
import org.optaplanner.core.api.score.stream.bi.BiConstraintStream;
import org.optaplanner.core.api.score.stream.tri.TriConstraintStream;
import org.optaplanner.core.api.score.stream.tri.TriJoiner;

public interface InnerBiConstraintStream<A, B> extends BiConstraintStream<A, B> {

    RetrievalSemantics getRetrievalSemantics();

    /**
     * This method will return true if the constraint stream is guaranteed to only produce distinct tuples.
     * See {@link #distinct()} for details.
     *
     * @return true if the guarantee of distinct tuples is provided
     */
    boolean guaranteesDistinct();

    @Override
    default <C> TriConstraintStream<A, B, C> join(Class<C> otherClass, TriJoiner<A, B, C>... joiners) {
        if (getRetrievalSemantics() == STANDARD) {
            return join(getConstraintFactory().forEach(otherClass), joiners);
        } else {
            return join(getConstraintFactory().from(otherClass), joiners);
        }
    }

    @Override
    default BiConstraintStream<A, B> distinct() {
        if (guaranteesDistinct()) {
            return this;
        } else {
            return groupBy((a, b) -> a, (a, b) -> b);
        }
    }

    @Override
    default BiConstraintBuilder<A, B> penalize(Score<?> constraintWeight, ToIntBiFunction<A, B> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.PENALTY);
    }

    @Override
    default BiConstraintBuilder<A, B> penalizeLong(Score<?> constraintWeight, ToLongBiFunction<A, B> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.PENALTY);
    }

    @Override
    default BiConstraintBuilder<A, B> penalizeBigDecimal(Score<?> constraintWeight, BiFunction<A, B, BigDecimal> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.PENALTY);
    }

    @Override
    default BiConstraintBuilder<A, B> penalizeConfigurable(ToIntBiFunction<A, B> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.PENALTY);
    }

    @Override
    default BiConstraintBuilder<A, B> penalizeConfigurableLong(ToLongBiFunction<A, B> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.PENALTY);
    }

    @Override
    default BiConstraintBuilder<A, B> penalizeConfigurableBigDecimal(BiFunction<A, B, BigDecimal> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.PENALTY);
    }

    @Override
    default BiConstraintBuilder<A, B> reward(Score<?> constraintWeight, ToIntBiFunction<A, B> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.REWARD);
    }

    @Override
    default BiConstraintBuilder<A, B> rewardLong(Score<?> constraintWeight, ToLongBiFunction<A, B> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.REWARD);
    }

    @Override
    default BiConstraintBuilder<A, B> rewardBigDecimal(Score<?> constraintWeight, BiFunction<A, B, BigDecimal> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.REWARD);
    }

    @Override
    default BiConstraintBuilder<A, B> rewardConfigurable(ToIntBiFunction<A, B> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.REWARD);
    }

    @Override
    default BiConstraintBuilder<A, B> rewardConfigurableLong(ToLongBiFunction<A, B> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.REWARD);
    }

    @Override
    default BiConstraintBuilder<A, B> rewardConfigurableBigDecimal(BiFunction<A, B, BigDecimal> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.REWARD);
    }

    @Override
    default BiConstraintBuilder<A, B> impact(Score<?> constraintWeight, ToIntBiFunction<A, B> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.MIXED);
    }

    @Override
    default BiConstraintBuilder<A, B> impactLong(Score<?> constraintWeight, ToLongBiFunction<A, B> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.MIXED);
    }

    @Override
    default BiConstraintBuilder<A, B> impactBigDecimal(Score<?> constraintWeight, BiFunction<A, B, BigDecimal> matchWeigher) {
        return innerImpact(constraintWeight, matchWeigher, ScoreImpactType.MIXED);
    }

    @Override
    default BiConstraintBuilder<A, B> impactConfigurable(ToIntBiFunction<A, B> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.MIXED);
    }

    @Override
    default BiConstraintBuilder<A, B> impactConfigurableLong(ToLongBiFunction<A, B> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.MIXED);
    }

    @Override
    default BiConstraintBuilder<A, B> impactConfigurableBigDecimal(BiFunction<A, B, BigDecimal> matchWeigher) {
        return innerImpact(null, matchWeigher, ScoreImpactType.MIXED);
    }

    BiConstraintBuilder<A, B> innerImpact(Score<?> constraintWeight, ToIntBiFunction<A, B> matchWeigher,
            ScoreImpactType scoreImpactType);

    BiConstraintBuilder<A, B> innerImpact(Score<?> constraintWeight, ToLongBiFunction<A, B> matchWeigher,
            ScoreImpactType scoreImpactType);

    BiConstraintBuilder<A, B> innerImpact(Score<?> constraintWeight, BiFunction<A, B, BigDecimal> matchWeigher,
            ScoreImpactType scoreImpactType);

    @Override
    default Constraint penalize(String constraintName, Score<?> constraintWeight) {
        return penalize(constraintWeight)
                .asConstraint(constraintName);
    }

    @Override
    default Constraint penalize(String constraintPackage, String constraintName, Score<?> constraintWeight) {
        return penalize(constraintWeight)
                .asConstraint(constraintPackage, constraintName);
    }

    @Override
    default Constraint penalizeConfigurable(String constraintName) {
        return penalizeConfigurable()
                .asConstraint(constraintName);
    }

    @Override
    default Constraint penalizeConfigurable(String constraintPackage, String constraintName) {
        return penalizeConfigurable()
                .asConstraint(constraintPackage, constraintName);
    }

    @Override
    default Constraint reward(String constraintName, Score<?> constraintWeight) {
        return reward(constraintWeight)
                .asConstraint(constraintName);
    }

    @Override
    default Constraint reward(String constraintPackage, String constraintName, Score<?> constraintWeight) {
        return reward(constraintWeight)
                .asConstraint(constraintPackage, constraintName);
    }

    @Override
    default Constraint rewardConfigurable(String constraintName) {
        return rewardConfigurable()
                .asConstraint(constraintName);
    }

    @Override
    default Constraint rewardConfigurable(String constraintPackage, String constraintName) {
        return penalizeConfigurable()
                .asConstraint(constraintPackage, constraintName);
    }

    @Override
    default Constraint impact(String constraintName, Score<?> constraintWeight) {
        return impact(constraintWeight)
                .asConstraint(constraintName);
    }

    @Override
    default Constraint impact(String constraintPackage, String constraintName, Score<?> constraintWeight) {
        return impact(constraintWeight)
                .asConstraint(constraintPackage, constraintName);
    }

}
