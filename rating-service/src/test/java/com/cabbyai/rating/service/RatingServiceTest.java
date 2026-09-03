package com.cabbyai.rating.service;

import com.cabbyai.rating.entity.Rating;
import com.cabbyai.rating.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitRatingSavesValidNewRating() {
        Rating rating = rating(5);
        when(ratingRepository.findByRideIdAndFromUserId(10L, 20L)).thenReturn(Optional.empty());
        when(ratingRepository.save(rating)).thenReturn(rating);

        assertSame(rating, ratingService.submitRating(rating));
        verify(ratingRepository).save(rating);
    }

    @Test
    void submitRatingRejectsInvalidScoreAndDuplicate() {
        assertThrows(RuntimeException.class, () -> ratingService.submitRating(rating(0)));
        verifyNoInteractions(ratingRepository);

        Rating duplicate = rating(4);
        when(ratingRepository.findByRideIdAndFromUserId(10L, 20L)).thenReturn(Optional.of(duplicate));
        assertThrows(RuntimeException.class, () -> ratingService.submitRating(duplicate));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void queryMethodsDelegateToRepository() {
        List<Rating> ratings = List.of(rating(4));
        when(ratingRepository.findByToUserIdOrderByCreatedAtDesc(30L)).thenReturn(ratings);
        when(ratingRepository.findByFromUserIdOrderByCreatedAtDesc(20L)).thenReturn(ratings);
        when(ratingRepository.findByRideId(10L)).thenReturn(ratings);
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(ratings.get(0)));

        assertEquals(ratings, ratingService.getUserRatings(30L));
        assertEquals(ratings, ratingService.getRatingsByUser(20L));
        assertEquals(ratings, ratingService.getRideRatings(10L));
        assertTrue(ratingService.getRatingById(1L).isPresent());
    }

    @Test
    void averageRatingReturnsZeroOrAverage() {
        when(ratingRepository.findByToUserId(30L)).thenReturn(List.of());
        assertEquals(0.0, ratingService.getAverageRating(30L));

        when(ratingRepository.findByToUserId(31L)).thenReturn(List.of(rating(5), rating(3)));
        assertEquals(4.0, ratingService.getAverageRating(31L));
    }

    @Test
    void updateRatingChangesProvidedFields() {
        Rating current = rating(3);
        Rating changes = new Rating();
        changes.setScore(5);
        changes.setComments("Excellent");
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(current));
        when(ratingRepository.save(current)).thenReturn(current);

        Rating result = ratingService.updateRating(1L, changes);

        assertEquals(5, result.getScore());
        assertEquals("Excellent", result.getComments());
        verify(ratingRepository).save(current);
    }

    @Test
    void updateRatingKeepsNullFieldsAndRejectsMissingRating() {
        Rating current = rating(3);
        Rating changes = new Rating();
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(current));
        when(ratingRepository.save(current)).thenReturn(current);

        ratingService.updateRating(1L, changes);

        assertEquals(3, current.getScore());
        verify(ratingRepository).save(current);
        when(ratingRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> ratingService.updateRating(2L, changes));
    }

    private Rating rating(int score) {
        return new Rating(10L, 20L, 30L, score, "Good", Rating.RatingType.USER_TO_DRIVER);
    }
}
