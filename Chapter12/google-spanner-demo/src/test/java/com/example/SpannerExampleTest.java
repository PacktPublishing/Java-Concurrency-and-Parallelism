package com.example;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ReadContext;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;

@RunWith(MockitoJUnitRunner.class)
public class SpannerExampleTest {

	@Mock
	private Spanner spanner;

	@Mock
	private DatabaseClient dbClient;

	@Mock
	private ReadContext readContext;

	@Mock
	private ResultSet resultSet;

	@InjectMocks
	private SpannerExample spannerExample;

	@Before
	public void setup() {
		// Mock SpannerOptions
		SpannerOptions spannerOptions = mock(SpannerOptions.class);
		when(spannerOptions.getService()).thenReturn(spanner);

		// Mock Spanner behavior
		when(spanner.getDatabaseClient(any(DatabaseId.class))).thenReturn(dbClient);
		when(dbClient.singleUse()).thenReturn(readContext);
		when(readContext.executeQuery(any(Statement.class))).thenReturn(resultSet);

		// Inject the mocked SpannerOptions into the SpannerExample
		spannerExample = new SpannerExample(spannerOptions);
	}

	@Test
	public void shouldRunQueryAndPrintResults() {
		// Prepare mock behavior
		when(resultSet.next()).thenReturn(true).thenReturn(false); // First call true, then false
		when(resultSet.getLong("UserId")).thenReturn(1L);
		when(resultSet.getString("Name")).thenReturn("John Doe");

		// Call the method under test
		spannerExample.run("mock-project-id", "mock-instance-id", "mock-database-id");

		// Verify interactions (adjusted)
		verify(spanner).getDatabaseClient(any(DatabaseId.class));
		verify(readContext).executeQuery(any(Statement.class));
		verify(resultSet, atLeastOnce()).next(); // Allow for multiple calls to next()
		verify(resultSet).getLong("UserId");
		verify(resultSet).getString("Name");
	}

}
