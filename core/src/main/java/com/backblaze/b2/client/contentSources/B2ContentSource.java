/*
 * Copyright 2017, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */

package com.backblaze.b2.client.contentSources;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementations of B2ContentSource provide the length, SHA1, and the
 * bytes that the B2Client should upload.
 *
 * There are common implementations of this class, such as B2FileContentSource
 * and MemoryContentSource.
 */
public interface B2ContentSource {
    /**
     * @return the number of bytes in the content.  the length must be non-negative.
     * @throws IOException if there's trouble
     */
    long getContentLength() throws IOException;

    /**
     * You are encouraged to implement this.  If it returns non-null,
     * the B2Client can provide the sha1 before performing the upload.
     *
     * Definitely implement this if you've stored the sha1 separately
     * from the file.  That way you can ensure that B2 doesn't take the
     * file if there's trouble reading it from your source.  If you
     * return null, B2StorageClient will compute the SHA1 from the bytes in the
     * stream.
     *
     * @return the hex-encoded sha1 for the content or null if it's not known yet.
     * @throws IOException if there's trouble
     */
    String getSha1OrNull() throws IOException;

    /**
     * @return the time the source was last modified (in milliseconds since the epoch)
     *         or null if there isn't a reasonable value for that.
     * @throws IOException if there's trouble
     */
    Long getSrcLastModifiedMillisOrNull() throws IOException;

    /**
     * NOTE: this may be called multiple times as uploads
     *       are retried, etc.  The content is expected to be identical
     *       each time this is called.
     * @return a new inputStream containing the contents.
     * @throws IOException if there's trouble
     */
    InputStream createInputStream() throws IOException;

    // XXX: is there a *range* version of createInputStream() to help with
    //      large files?
}
