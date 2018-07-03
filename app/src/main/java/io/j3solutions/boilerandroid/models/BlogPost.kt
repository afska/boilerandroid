package io.j3solutions.boilerandroid.models

data class BlogPost(
    val id: Int,
    val date: String,
    val date_gmt: String,
    val guid: Guid,
    val modified: String,
    val modified_gmt: String,
    val slug: String,
    val status: String,
    val type: String,
    val link: String,
    val title: Title,
    val content: Content,
    val excerpt: Excerpt,
    val author: Int,
    val featured_media: Int,
    val comment_status: String,
    val ping_status: String,
    val sticky: Boolean,
    val template: String,
    val format: String,
    val meta: List<Any>,
    val categories: List<Int>,
    val tags: List<Any>,
    val _links: Links
)

data class Content(
    val rendered: String,
    val protected: Boolean
)

data class Title(
    val rendered: String
)

data class Guid(
    val rendered: String
)

data class Links(
    val self: List<Self>,
    val collection: List<Collection>,
    val about: List<About>,
    val author: List<Author>,
    val replies: List<Reply>,
    val curies: List<Cury>
)

data class Self(
    val href: String
)

data class Wpattachment(
    val href: String
)

data class About(
    val href: String
)

data class Cury(
    val name: String,
    val href: String,
    val templated: Boolean
)

data class Collection(
    val href: String
)

data class Reply(
    val embeddable: Boolean,
    val href: String
)

data class Author(
    val embeddable: Boolean,
    val href: String
)

data class VersionHistory(
    val href: String
)

data class Wpterm(
    val taxonomy: String,
    val embeddable: Boolean,
    val href: String
)

data class Excerpt(
    val rendered: String,
    val protected: Boolean
)