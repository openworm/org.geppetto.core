package org.geppetto.core.datasources;

/**
 * Request-scoped paging parameters (offset/limit) for a single query execution.
 *
 * The client sends offset/limit on the run_query websocket message; the frontend
 * (WebsocketConnection) sets them here on the request thread immediately before
 * the synchronous query runs, and the datasource query executor
 * (ExecuteQueryVisitor) reads them to page the underlying query. Values are
 * per-thread and MUST be cleared after each query so they never leak to the next
 * request on a pooled thread.
 */
public final class QueryPagingContext
{
	private static final ThreadLocal<Integer> OFFSET = new ThreadLocal<Integer>();
	private static final ThreadLocal<Integer> LIMIT = new ThreadLocal<Integer>();

	private QueryPagingContext()
	{
	}

	public static void set(Integer offset, Integer limit)
	{
		OFFSET.set(offset);
		LIMIT.set(limit);
	}

	public static void clear()
	{
		OFFSET.remove();
		LIMIT.remove();
	}

	/** Offset for the current query, or 0 when unset/invalid. */
	public static int getOffset()
	{
		Integer o = OFFSET.get();
		return (o != null && o.intValue() > 0) ? o.intValue() : 0;
	}

	/** Limit for the current query, or {@code defaultLimit} when unset/invalid. */
	public static int getLimit(int defaultLimit)
	{
		Integer l = LIMIT.get();
		return (l != null && l.intValue() > 0) ? l.intValue() : defaultLimit;
	}
}
