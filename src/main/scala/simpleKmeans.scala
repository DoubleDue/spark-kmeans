package main

object simpleKmeans {
  def main(args: Array[String]) {
    //parameter
    def k = 2
    def convergeDist = 0.1

    /** Input **/
    //val points = Array.fill(100000) { Point.random }
    val points = Array(
    new Point(1.0, 1.0),
    new Point(1.5, 2.0),
    new Point(3.0, 4.0),
    new Point(5.0, 7.0),
    new Point(3.5, 5.0),
    new Point(4.5, 5.0),
    new Point(3.5, 4.5)
)

    /** Initialization **/
    //val centroids = Array.fill(k) { Point.random }
    val centroids = Array (new Point(1.5, 5.0), new Point(2.5, 3.0), new Point(1.5, 1.0))
    val resultCentroids = kmeans(points, centroids, convergeDist)
    println(resultCentroids)
  }

  def kmeans(points: Seq[Point], centroids: Seq[Point], convergeDist: Double): Seq[Point] = {
    
    def closestCentroid(centroids: Seq[Point], point: Point) = {
      centroids.reduceLeft(
        //search for min distance
        (a, b) => if ((point distance a) < (point distance b)) a else b)
    }

    println(centroids)
    /** Assignnment Step **/
    //group points to closest centroid
    //output is < k, [v1,.,vn] >
    //k: centroid
    //[v1,.,vn]: closest point to centroids
    val pointGroups = points.groupBy(closestCentroid(centroids, _))

    /** Update Step **/
    // Recompute new centroids of each cluster as the average of the points in their cluster
    //note: if the group of points associated to a centroid is empty, the centroid doesn't change
    val newCentroids = centroids.map(oldCentroid => {
      pointGroups.get(oldCentroid) match {
        case Some(pointsInCluster) => pointsInCluster.reduceLeft(_ + _) / pointsInCluster.length
        case None => oldCentroid
      }})

    /** Stopping condition **/
    // Calculate the centroid movement
    val movement = (centroids zip newCentroids).map({
      case (oldCentroid, newCentroid) => oldCentroid distance newCentroid
    })

    // Repeat if movement exceeds threshold
    if (movement.exists(_ > convergeDist))
      kmeans(points, newCentroids, convergeDist)
    else
      return newCentroids
  }

}

class Point(myX: Double, myY: Double) {
  //members
  val x = myX
  val y = myY
  //relaxed operators
  def +(that: Point) = new Point(this.x + that.x, this.y + that.y)
  def -(that: Point) = this + (-that)
  def unary_-() = new Point(-this.x, -this.y)
  def /(d: Double) = new Point(this.x / d, this.y / d)
  //to define according to distance (Euclidean distance)
  def magnitude = math.sqrt(x * x + y * y)
  def distance(that: Point) = (that - this).magnitude

  override def toString = format("(%.2f - %.2f)", x, y)
}

object Point {
  //for random points
  def random() = new Point(math.random * 10, math.random * 10)
}